/*
 * Copyright (c) 2018 William Frank (info@williamfrank.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from '@angular/common/http';
import {HttpRequestMapping, MethodParamMapping} from "./api";
import {Observable} from "rxjs/internal/Observable";
import {error} from "@angular/compiler/src/util";


@Injectable()
export class ServiceRequestManager {

  constructor(private httpClient:HttpClient) { }

  public execute<T>(defaultMapping:HttpRequestMapping, requestMapping:HttpRequestMapping, params: MethodParamMapping[])
    : Observable<T> {
    const urlTemplate:string = this.concatUrl(defaultMapping.urlTemplate, requestMapping.urlTemplate, params);
    const httpParams = this.prepareHttpParams(params);
    let body = this.extractBody(params);
    let method:string = requestMapping.method ? requestMapping.method : defaultMapping.method;
      return this.request(method, urlTemplate, httpParams, body);
  }

  public request<T>(method: string, urlTemplate: string, httpParams, body): Observable<T> {
      return this.httpClient.request<T>(method, urlTemplate, {
          params: httpParams,
          body: body
      });
  }

  private extractBody(params: MethodParamMapping[]) {
    const bodyParams = params
      .filter(p => p.isRequestBody)
      .filter(p => p.value)
    ;
    if (bodyParams.length > 1) {
      throw error("Unable to handle multiple body params. Received " + bodyParams.length + " body arguments");
    }

    let body: any = bodyParams.length > 0 ? bodyParams[0].value : undefined;
    return body;
  }

private prepareHttpParams(params: MethodParamMapping[]) {
    let httpParams: HttpParams = new HttpParams();
    for (let index:number = 0; index < params.length; ++index) {
        let param: MethodParamMapping = params[index];
        if (param.requestParameterName && (!param.isRequestBody)) {
            httpParams = httpParams.append(param.requestParameterName, param.value);
        }
    }
    return httpParams;
}

  private concatUrl(baseMapping: string, requestMapping: string, params: MethodParamMapping[]):string {
    let url: string = baseMapping;
    if (!url.endsWith("/")) {
      url += "/";
    }
    if (requestMapping.startsWith("/")) {
      url += requestMapping.substring(1);
    } else {
      url += requestMapping;
    }

    params
      .filter(p => p.pathVariableName)
      .forEach(p => {
        url = url.replace("{" + p.pathVariableName + "}", encodeURI(p.value));
      });

    return url;
  }
}
