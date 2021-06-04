/*
 * Copyright 2021-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.spring.example.controllers;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.model.TokenType;
import com.okta.idx.sdk.api.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    /**
     * logger instance.
     */
    private final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    /**
     * idx client instance.
     */
    @Autowired
    private IDXAuthenticationWrapper idxAuthenticationWrapper;

    /**
     * Handle logout by revoking the access token and invalidating the session.
     *
     * @param session the session
     * @return the redirection to login view
     */
    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        logger.info(":: Logout ::");

        // retrieve access token
        TokenResponse tokenResponse =
                (TokenResponse) session.getAttribute("tokenResponse");

        if (tokenResponse != null) {
            String accessToken = tokenResponse.getAccessToken();
            // revoke access token
            logger.info("Revoking access token");
            idxAuthenticationWrapper.revokeToken(TokenType.ACCESS_TOKEN, accessToken);
        }

        // invalidate session
        session.invalidate();
        return "redirect:/";
    }
}
