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

import net.revelc.gnome.keyring.lib.GLib2;
import net.revelc.gnome.keyring.lib.GnomeKeyringItemType;
import net.revelc.gnome.keyring.lib.GnomeKeyringLibrary;
import net.revelc.gnome.keyring.lib.GnomeKeyringResult;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * 
 */
public class GnomeKeyring {

  private GLib2 glib2;
  private GnomeKeyringLibrary gklib;

  /**
   * 
   */
  public GnomeKeyring() {
    try {
      glib2 = (GLib2) Native.loadLibrary("glib-2.0", GLib2.class);
      glib2.g_set_application_name("GnomeKeyringJava");
    } catch (UnsatisfiedLinkError e) {
      // Don't really care that much
      System.err.println("** WARNING: " + e.getMessage());
    }
    gklib = (GnomeKeyringLibrary) Native.loadLibrary("gnome-keyring", GnomeKeyringLibrary.class);
  }

  public GnomeKeyring(String applicationName) {}

  public GnomeKeyringItem getItem(String keyring, int id, boolean includeSecret) throws GnomeKeyringException {
    PointerByReference item_info_ref = new PointerByReference();
    Pointer item_info = null;
    try {
      int r = gklib.gnome_keyring_item_get_info_full_sync(keyring, id, includeSecret ? 1 : 0, item_info_ref);
      GnomeKeyringResult result = GnomeKeyringResult.fromValue(r);
      switch (result) {
        case GNOME_KEYRING_RESULT_OK:
          item_info = item_info_ref.getValue();
          String type = GnomeKeyringItemType.fromValue(gklib.gnome_keyring_item_info_get_type(item_info)).toString();
          String display = gklib.gnome_keyring_item_info_get_display_name(item_info);
          String secret = gklib.gnome_keyring_item_info_get_secret(item_info);
          Date ctime = new Date(gklib.gnome_keyring_item_info_get_ctime(item_info) * 1000);
          Date mtime = new Date(gklib.gnome_keyring_item_info_get_mtime(item_info) * 1000);
          return new GnomeKeyringItem(gklib, keyring, id, type, display, secret, ctime, mtime);
        default:
          throw new GnomeKeyringException(result.toString());
      }
    } finally {
      if (item_info != null)
        gklib.gnome_keyring_item_info_free(item_info);
    }
  }
}
