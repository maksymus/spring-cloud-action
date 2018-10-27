package org.cloud.action.license.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.cloud.action.license.client.OrganizationFeignClient;
import org.cloud.action.license.client.OrganizationRestTemplateClient;
import org.cloud.action.license.config.ServiceConfig;
import org.cloud.action.license.model.License;
import org.cloud.action.license.model.Organization;
import org.cloud.action.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig config;

    @Autowired
    private OrganizationFeignClient organizationFeignClient;

    @Autowired
    private OrganizationRestTemplateClient organizationRestClient;

//    @Autowired
//    private OrganizationDiscoveryClient organizationDiscoveryClient;

    private Organization retrieveOrgInfo(String organizationId, String clientType){
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
//                System.out.println("I am using the discovery client");
//                organization = organizationDiscoveryClient.getOrganization(organizationId);
//                break;
                throw new RuntimeException("not supported");
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    public License getLicense(String organizationId,String licenseId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId, clientType);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(
                organizationId, licenseId);
        return license.withComment(config.getExampleProperty());
    }

    public void saveLicense(License license){
        license.withId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), // timeout
                    @HystrixProperty(name = "coreSize",value="30"), // thread pool size
                    @HystrixProperty(name="maxQueueSize", value="10") // thread pool queue
            },
            threadPoolKey = "licenseByOrgThreadPool", // thread pool name
            fallbackMethod = "buildFallbackLicenseList" // fallback method name
    )
    public List<License> getLicensesByOrg(String organizationId){
        simulateLongRun();
        return licenseRepository.findByOrganizationId( organizationId );
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId(organizationId)
                .withProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    private void simulateLongRun() {
        int i = new Random().nextInt(3);

        if (i == 0) {
            try {
                Thread.sleep(11000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}