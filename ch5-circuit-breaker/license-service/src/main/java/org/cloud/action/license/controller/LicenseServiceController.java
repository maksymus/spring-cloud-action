package org.cloud.action.license.controller;

import org.cloud.action.license.model.License;
import org.cloud.action.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        return licenseService.getLicensesByOrg(organizationId);
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public License getLicenses( @PathVariable("organizationId") String organizationId,
                                @PathVariable("licenseId") String licenseId) {
        return licenseService.getLicense(organizationId,licenseId);
    }

    @RequestMapping(value="{licenseId}", method = RequestMethod.PUT)
    public String updateLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the put");
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @RequestMapping(value="{licenseId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicenses( @PathVariable("licenseId") String licenseId) {
        return String.format("This is the Delete");
    }

    @RequestMapping(value="/{licenseId}/{clientType}", method = RequestMethod.GET)
    public License getLicensesWithClient( @PathVariable("organizationId") String organizationId,
                                          @PathVariable("licenseId") String licenseId,
                                          @PathVariable("clientType") String clientType) {
        return licenseService.getLicense(organizationId, licenseId, clientType);
    }
}
