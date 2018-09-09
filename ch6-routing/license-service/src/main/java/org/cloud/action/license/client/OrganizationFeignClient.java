package org.cloud.action.license.client;

import org.cloud.action.license.model.Organization;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//"http://routing-server/api/organization-service/v1/organizations/{organizationId}",

@FeignClient("routing-server")
@RequestMapping("/api/organization-service")
public interface OrganizationFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/v1/organizations/{organizationId}",
            consumes="application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
