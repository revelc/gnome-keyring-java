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

import java.util.Date;

import net.revelc.gnome.keyring.lib.GnomeKeyringLibrary;
import net.revelc.gnome.keyring.lib.GnomeKeyringResult;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * 
 */
public class GnomeKeyringItem {

  private GnomeKeyringLibrary gklib;
  private String keyring;
  private int id;

  private String type, displayName, secret;
  private Date ctime, mtime;

  GnomeKeyringItem(GnomeKeyringLibrary gklib, String keyring, int id, String type, String displayName, String secret, Date ctime, Date mtime) {
    this.gklib = gklib;
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

  public String[] getAttributes() throws GnomeKeyringException {
    PointerByReference attributes_ref = new PointerByReference();
    Pointer attributes = null;
    try {
      int r = gklib.gnome_keyring_item_get_attributes_sync(keyring, id, attributes_ref);
      GnomeKeyringResult result = GnomeKeyringResult.fromValue(r);
      switch (result) {
        case GNOME_KEYRING_RESULT_OK:
          attributes = attributes_ref.getValue();
          return null;
        default:
          throw new GnomeKeyringException(result.toString());
      }
    } finally {
      if (attributes != null)
        gklib.gnome_keyring_item_info_free(attributes);
    }
  }

}
