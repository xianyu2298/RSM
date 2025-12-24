package edu.jjxy.researchmanagementsystem.service;

import edu.jjxy.researchmanagementsystem.dto.LoginReq;
import edu.jjxy.researchmanagementsystem.dto.LoginResp;

public interface AuthService {
    LoginResp login(LoginReq req);
}
