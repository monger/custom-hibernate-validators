package io.monger.validation.resource

import io.monger.validation.IntegrationBootstrap
import io.monger.validation.model.Combined
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
 * Test the "combined" REST endpoints.
 */
@Stepwise
class CombinedResourceTest extends IntegrationBootstrap {
    def getBasePath() {
        'combined'
    }

    @Test @Unroll
    void 'test adding valid combined models'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Combined>(new Combined(
                            id: 0,
                            domainName: _domain,
                            ipAddress: _ip,
                            port: _port
                    )),
                    Void.class)
        then:
            result.statusCode == _status
        where:
            _domain             | _ip              | _port || _status
            'google.com'        | '216.58.194.206' | 80    || HttpStatus.NO_CONTENT
            'bing.com'          | '204.79.197.200' | 443   || HttpStatus.NO_CONTENT
            'code.ninja'        | '127.0.0.1'      | 8080  || HttpStatus.NO_CONTENT
            'something.someday' | '192.168.0.1'    | 8443  || HttpStatus.NO_CONTENT
    }

    @Test
    void 'test getting the combined model list'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.GET,
                    null,
                    List.class)
        then:
            result.body.size() == 4
    }

    @Test
    void 'test getting a single model'() {
        when:
            def result = template.exchange(
                    URI.create("${serviceURI().toString()}/1"),
                    HttpMethod.GET,
                    null,
                    Combined.class)
        then:
            result.body.domainName == 'google.com'
    }

    @Test
    void 'test updating a model'() {
        when:
            def result = template.exchange(
                    URI.create("${serviceURI().toString()}/1"),
                    HttpMethod.POST,
                    new HttpEntity<Combined>(new Combined(
                            id: 1,
                            domainName: 'sub.google.com',
                            ipAddress: '216.58.194.206',
                            port: 80
                    )),
                    Combined.class
            )
        then:
            result.body.domainName == 'sub.google.com'
    }

    @Test
    void 'test deleting a model'() {
        when:
            def deleteResult = template.exchange(
                    URI.create("${serviceURI().toString()}/1"),
                    HttpMethod.DELETE,
                    null,
                    Void.class
            )
        and:
            def getResult = template.exchange(
                    URI.create("${serviceURI().toString()}/1"),
                    HttpMethod.GET,
                    null,
                    Combined.class)
        then:
            deleteResult.statusCode == HttpStatus.NO_CONTENT && getResult.statusCode == HttpStatus.NOT_FOUND
    }

    @Test @Unroll
    void 'test creating invalid combined models'() {
        when:
            def result = template.exchange(
                    serviceURI(),
                    HttpMethod.PUT,
                    new HttpEntity<Combined>(new Combined(
                            id: _id,
                            domainName: _domain,
                            ipAddress: _ip,
                            port: _port
                    )),
                    Void.class)
        then:
            result.statusCode == _status
        where:
            _id  | _domain             | _ip              | _port || _status
            0    | 'H4X0R3D'           | '127.0.0.1'      | 80    || HttpStatus.NOT_FOUND
            8080 | 'google.com'        | '192.168.0.1'    | 443   || HttpStatus.NOT_FOUND
            1    | 'some.domain'       | '127.0.0.1'      | 8080  || HttpStatus.NOT_FOUND
            -1   | '42'                | 'something'      | -3    || HttpStatus.NOT_FOUND
    }

    @Test @Unroll
    void 'test updating models that do not exist'() {
        when:
            def result = template.exchange(
                    URI.create("${serviceURI().toString()}/$_id"),
                    HttpMethod.POST,
                    new HttpEntity<Combined>(new Combined(
                            id: _id,
                            domainName: _domain,
                            ipAddress: _ip,
                            port: _port
                    )),
                    Combined.class)
        then:
            result.statusCode == _status
        where:
            _id  | _domain             | _ip              | _port || _status
            10   | 'google.com'        | '216.58.194.206' | 80    || HttpStatus.NOT_FOUND
            11   | 'bing.com'          | '204.79.197.200' | 443   || HttpStatus.NOT_FOUND
            100  | 'code.ninja'        | '127.0.0.1'      | 8080  || HttpStatus.NOT_FOUND
            1000 | 'something.someday' | '192.168.0.1'    | 8443  || HttpStatus.NOT_FOUND
    }

    @Test @Unroll
    void 'test updating existing models with invalid data'() {
        when:
            def result = template.exchange(
                    URI.create("${serviceURI().toString()}/$_id"),
                    HttpMethod.POST,
                    new HttpEntity<Combined>(new Combined(
                            id: _id,
                            domainName: _domain,
                            ipAddress: _ip,
                            port: _port
                    )),
                    Combined.class)
        then:
            result.statusCode == _status
        where:
            _id  | _domain             | _ip              | _port || _status
            2    | 'H4X0R3D'           | '127.0.0.1'      | 80    || HttpStatus.NOT_FOUND
            3    | 'google.com'        | '559.168.0.1'    | 443   || HttpStatus.NOT_FOUND
            3    | 'some.domain'       | '127.0.0.1'      | 66000 || HttpStatus.NOT_FOUND
            4    | '42'                | 'something'      | -3    || HttpStatus.NOT_FOUND
    }
}
