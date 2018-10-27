package org.cloud.action.organization.service;

import org.cloud.action.organization.event.source.SimpleSourceBean;
import org.cloud.action.organization.model.Organization;
import org.cloud.action.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    private SimpleSourceBean simpleSourceBean;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId);
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());
        orgRepository.save(org);

        simpleSourceBean.publishOrgChange("SAVE", org.getId());
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("UPDATE", org.getId());
    }

    public void deleteOrg(Organization org){
        orgRepository.delete( org.getId());
        simpleSourceBean.publishOrgChange("DELETE", org.getId());
    }
}
