/*
 * Copyright (C) 2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.spray
package encoding

import http._
import HttpHeaders._

trait Encoder {
  def encoding: HttpEncoding
  
  def handle(response: HttpResponse): Boolean
  
  def encode(response: HttpResponse): HttpResponse = response.content match {
    case Some(content) if !response.isEncodingSpecified && handle(response) => response.copy(
      headers = `Content-Encoding`(encoding) :: response.headers,
      content = Some(HttpContent(content.contentType, encodeBuffer(content.buffer)))
    )
    case _ => response
  }

  def encodeBuffer(buffer: Array[Byte]): Array[Byte]
}