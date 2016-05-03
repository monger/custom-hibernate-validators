package io.monger.validation.resource

import io.monger.validation.IntegrationBootstrap
import org.junit.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Stepwise
import spock.lang.Unroll

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
 * Tests the IP Address resource REST endpoints.
 */
@Stepwise
class IpAddressResourceTest extends IntegrationBootstrap {

    def getBasePath() {
        'ipaddress'
    }

    @Test
    void 'test getting the IP address'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            result.body == '127.0.0.1'
    }

    @Test @Unroll
    void 'test setting valid IP addresses'() {
        when:
            def putResult = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(_ipAddress),
                    Void.class)
        and:
            def getResult = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            putResult.statusCode == _status && getResult.body == _ipAddress
        where:
            _ipAddress        || _status
            '192.168.0.1'     || HttpStatus.NO_CONTENT
            '4.4.4.4'         || HttpStatus.NO_CONTENT
            '8.8.8.8'         || HttpStatus.NO_CONTENT
            '255.255.255.255' || HttpStatus.NO_CONTENT
            '1.1.1.1'         || HttpStatus.NO_CONTENT
    }

    @Test @Unroll
    void 'test setting invalid IP addresses'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(_ipAddress),
                    Void.class
            )
        then:
            result.statusCode == _status
        where:
            _ipAddress        || _status
            '256.256.256.256' || HttpStatus.NOT_FOUND
            '1000.50.86.8'    || HttpStatus.NOT_FOUND
            '-1.1.1.1'        || HttpStatus.NOT_FOUND
            'banana'          || HttpStatus.NOT_FOUND
    }
}
