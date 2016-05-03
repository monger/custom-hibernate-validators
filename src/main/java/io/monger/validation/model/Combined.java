package io.monger.validation.model;

import io.monger.validation.validator.DomainName;
import io.monger.validation.validator.IpAddress;
import io.monger.validation.validator.Port;

/*
 * Copyright (c) 2016 Phillip Babbitt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Model that contains a domain name, IP address and network port.
 */
public class Combined {
    private Integer id;
    @DomainName
    private String domainName;
    @IpAddress
    private String ipAddress;
    @Port
    private Integer port;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }
}
