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
 * Tests the domain name resource REST endpoints.
 */
@Stepwise
class DomainResourceTest extends IntegrationBootstrap {
    def getBasePath() {
        'domainname'
    }

    @Test
    void 'test getting the domain name'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            result.body == 'monger.io'
    }

    @Test @Unroll
    void 'test setting valid domain names'() {
        when:
            def putResult = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(_domain),
                    Void.class)
        and:
            def getResult = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    String.class)
        then:
            putResult.statusCode == _status && getResult.body == _domain
        where:
            _domain             || _status
            'google.com'        || HttpStatus.NO_CONTENT
            'bing.com'          || HttpStatus.NO_CONTENT
            'code.ninja'        || HttpStatus.NO_CONTENT
            'something.someday' || HttpStatus.NO_CONTENT
            'sub.domain.name'   || HttpStatus.NO_CONTENT
    }

    @Test @Unroll
    void 'test setting invalid domain names'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Object>(_domain),
                    Void.class
            )
        then:
            result.statusCode == _status
        where:
            _domain             || _status
            '256.256.256.256'   || HttpStatus.NOT_FOUND
            BigInteger.ZERO     || HttpStatus.NOT_FOUND
            '<script></script>' || HttpStatus.NOT_FOUND
            'H4X0R3D'           || HttpStatus.NOT_FOUND
    }
}
