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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.revelc.gnome.keyring.lib.GnomeKeyringAttribute;
import net.revelc.gnome.keyring.lib.GnomeKeyringAttributeList;
import net.revelc.gnome.keyring.lib.GnomeKeyringLibrary;
import net.revelc.gnome.keyring.lib.GnomeKeyringResult;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * 
 */
public class GnomeKeyringItem {

  private String keyring;
  private int id;

  private String type, displayName, secret;
  private Date ctime, mtime;

  GnomeKeyringItem(String keyring, int id, String type, String displayName, String secret, Date ctime, Date mtime) {
    this.keyring = keyring;
    this.id = id;
    this.type = type;
    this.displayName = displayName;
    this.secret = secret;
    this.ctime = ctime;
    this.mtime = mtime;
  }

  public String getType() {
    return type;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getSecret() {
    return secret;
  }

  public Date getCTime() {
    return ctime;
  }

  public Date getMTime() {
    return mtime;
  }

  public Map<String,String> getAttributes() throws GnomeKeyringException {
    PointerByReference pref = new PointerByReference();
    GnomeKeyringLibrary gklib = GnomeKeyring.getInstance();
    GnomeKeyringResult result = new GnomeKeyringResult(gklib.gnome_keyring_item_get_attributes_sync(keyring, id, pref));
    if (result.success()) {
      Pointer p = pref.getValue();
      GnomeKeyringAttributeList gkal = new GnomeKeyringAttributeList(p);
      if (gkal.len > 0) {
        GnomeKeyringAttribute attrib = new GnomeKeyringAttribute(gkal.data);
        GnomeKeyringAttribute[] attribArray = (GnomeKeyringAttribute[]) attrib.toArray(gkal.len);
        HashMap<String,String> attributes = new HashMap<String,String>();
        for (GnomeKeyringAttribute gka : attribArray)
          attributes.put(gka.name, gka.getValue());
        gklib.gnome_keyring_attribute_list_free(p);
        return attributes;
      } else {
        return Collections.emptyMap();
      }
    } else {
      return result.error();
    }
  }
}
