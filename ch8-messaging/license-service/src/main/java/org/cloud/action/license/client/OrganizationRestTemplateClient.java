package org.cloud.action.license.client;

import org.cloud.action.license.model.Organization;
import org.cloud.action.license.repository.OrganizationRedisRepository;
import org.cloud.action.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrganizationRedisRepository orgRedisRepo;


    public Organization getOrganization(String organizationId) {
        Organization org = checkRedisCache(organizationId);

        if (org != null) {
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, org);
            return org;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://routing-server/api/organization-service/v1/organizations/{organizationId}",
//                        "http://routing-server:5555/api/organization-service/v1/organizations/{organizationId}",
//                        "http://organization-service/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        org = restExchange.getBody();

        if (org != null) {
            cacheOrganizationObject(org);
        }

        return restExchange.getBody();
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        } catch (Exception ex) {
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}",
                    organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        } catch (Exception ex) {
            logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
        }
    }
}
