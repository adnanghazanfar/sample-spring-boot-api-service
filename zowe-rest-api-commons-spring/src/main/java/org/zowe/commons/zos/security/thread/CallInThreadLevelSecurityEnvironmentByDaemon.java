/*
 * This program and the accompanying materials are made available and may be used, at your option, under either:
 * * Eclipse Public License v2.0, available at https://www.eclipse.org/legal/epl-v20.html, OR
 * * Apache License, version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package org.zowe.commons.zos.security.thread;

import java.util.concurrent.Callable;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.zowe.commons.zos.security.service.PlatformSecurityService;

public final class CallInThreadLevelSecurityEnvironmentByDaemon<T> implements Callable<T> {
    private final PlatformSecurityService service;
    private final Callable<T> callable;
    private final Authentication authentication;

    public CallInThreadLevelSecurityEnvironmentByDaemon(PlatformSecurityService service, Callable<T> callable,
            Authentication authentication) {
        Assert.notNull(service, "service cannot be null");
        Assert.notNull(callable, "callable cannot be null");
        Assert.notNull(authentication, "authentication cannot be null");
        this.service = service;
        this.callable = callable;
        this.authentication = authentication;
    }

    @Override
    public T call() throws Exception {
        createSecurityEnvironment();
        try {
            return callable.call();
        } finally {
            service.removeThreadSecurityContext();
        }
    }

    private void createSecurityEnvironment() {
        service.createThreadSecurityContextByDaemon(authentication.getName(), null);
    }

    @Override
    public String toString() {
        return callable.toString();
    }
}
