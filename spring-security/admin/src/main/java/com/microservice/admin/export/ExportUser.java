package com.microservice.admin.export;

import com.microservice.admin.service.UsersService;
import com.microservice.core.admin.constant.ApiUrl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.URL_ADMIN)
public class ExportUser {


    private final UsersService usersService;

    public ExportUser(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/export/admin")
    public ResponseEntity<ByteArrayResource> export(@RequestParam(value = "typeFile", required = false) String typeFile
            , @RequestParam (value = "fileName", required = false) String fileName
    ) {
        return usersService.export(typeFile,fileName);
    }

}
