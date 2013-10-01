/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.gnome.keyring;

import java.util.Map.Entry;

/**
 * 
 */
public class Example {

  public static void main(String[] args) throws GnomeKeyringException {
    GnomeKeyring gk = new GnomeKeyring();
    GnomeKeyringItem item = gk.getItem("login", 4, true);
    int keyLength = 20;
    String formatString = "%-" + keyLength + "s: %s\n";
    System.out.printf(formatString, "Type", item.getType());
    System.out.printf(formatString, "DisplayName", item.getDisplayName());
    System.out.printf(formatString, "Secret", item.getSecret());
    System.out.printf(formatString, "CTime", item.getCTime());
    System.out.printf(formatString, "MTime", item.getMTime());
    for (Entry<String,String> attrib : item.getAttributes().entrySet()) {
      System.out.printf(formatString, "Attrib:" + attrib.getKey(), attrib.getValue());
    }
  }
}
