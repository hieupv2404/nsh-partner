/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.nextpay.nextshop.service.HealthService;

@RestController
@RequestMapping("/healthcheck")
@CrossOrigin(origins = "*", maxAge = 3600)

public class HealthController {
    @Autowired
    private HealthService healthService;

    @GetMapping("/liveness")
    public void healthLiveness() {
    }

    @GetMapping("/readiness")
    public void healthReadiness() {
    }

    @GetMapping("/unhealthy")
    public void unhealthly() {
        healthService.unhealthy();
    }
}
