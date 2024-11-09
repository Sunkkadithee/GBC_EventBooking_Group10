package ca.gbc.approvalservice;

import ca.gbc.approvalservice.service.ApprovalService;
import org.springframework.boot.SpringApplication;

public class TestApprovalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(ApprovalService::main).with(TestcontainersConfiguration.class).run(args);
    }

}
