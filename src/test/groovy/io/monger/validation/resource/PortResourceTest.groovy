package io.monger.validation.resource

import io.monger.validation.IntegrationBootstrap
import org.junit.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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
 * Tests the Port resource REST endpoints.
 */
class PortResourceTest extends IntegrationBootstrap {

    def getBasePath() {
        'port'
    }

    @Test
    void 'test getting the port number'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            result.body == '8080'
    }

    @Test @Unroll
    void 'test setting valid port number'() {
        when:
            def putResult = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(new Integer(_port).toString()),
                    Void.class)
        and:
            def getResult = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            putResult.statusCode == _status && getResult.body == Integer.toString(_port)
        where:
            _port || _status
            1     || HttpStatus.NO_CONTENT
            80    || HttpStatus.NO_CONTENT
            443   || HttpStatus.NO_CONTENT
            8080  || HttpStatus.NO_CONTENT
            65535 || HttpStatus.NO_CONTENT
    }

    @Test @Unroll
    void 'test setting invalid port number'() {
        when:
            def result = template.exchange(
                serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(new Integer(_port).toString()),
                    Void.class
            )
        then:
            result.statusCode == _status
        where:
            _port   || _status
            0       || HttpStatus.NOT_FOUND
            65536   || HttpStatus.NOT_FOUND
            1000000 || HttpStatus.NOT_FOUND
    }
}
