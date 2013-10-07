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
import java.util.Set;
import java.util.TreeSet;

import net.revelc.gnome.keyring.GnomeKeyringItem.Attribute;
import net.revelc.gnome.keyring.lib.GKAttributeList;
import net.revelc.gnome.keyring.lib.GKAttributeStruct;
import net.revelc.gnome.keyring.lib.GKItemType;
import net.revelc.gnome.keyring.lib.GKLib;
import net.revelc.gnome.keyring.lib.GKResult;
import net.revelc.gnome.keyring.lib.GLib2;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * 
 */
public class GnomeKeyring {

  private final GLib2 glib2;
  private final GKLib gklib;

  /**
   * 
   */
  public GnomeKeyring() {
    this("GnomeKeyringJava");
  }

  public GnomeKeyring(String applicationName) {
    glib2 = (GLib2) Native.loadLibrary("glib-2.0", GLib2.class);
    gklib = (GKLib) Native.loadLibrary("gnome-keyring", GKLib.class);
    glib2.g_set_application_name(applicationName);
  }

  public GnomeKeyringItem getItem(String keyring, int id, boolean includeSecret) throws GnomeKeyringException {
    PointerByReference item_info_ref = new PointerByReference();
    Pointer item_info = null;
    try {
      GKResult result = new GKResult(gklib, gklib.gnome_keyring_item_get_info_full_sync(keyring, id, includeSecret ? 1 : 0, item_info_ref));
      if (result.success()) {
        item_info = item_info_ref.getValue();
        String type = GKItemType.fromValue(gklib.gnome_keyring_item_info_get_type(item_info)).toString();
        String display = gklib.gnome_keyring_item_info_get_display_name(item_info);
        String secret = gklib.gnome_keyring_item_info_get_secret(item_info);
        Date ctime = new Date(gklib.gnome_keyring_item_info_get_ctime(item_info) * 1000);
        Date mtime = new Date(gklib.gnome_keyring_item_info_get_mtime(item_info) * 1000);
        Set<Attribute<?>> attributes = getItemAttributes(keyring, id);
        return new GnomeKeyringItem(type, display, secret, ctime, mtime, attributes);
      } else {
        return result.error();
      }
    } finally {
      if (item_info != null)
        gklib.gnome_keyring_item_info_free(item_info);
    }
  }

  private Set<Attribute<?>> getItemAttributes(String keyring, int id) throws GnomeKeyringException {
    Set<Attribute<?>> attributes = new TreeSet<Attribute<?>>();
    PointerByReference pref = new PointerByReference();
    GKResult result = new GKResult(gklib, gklib.gnome_keyring_item_get_attributes_sync(keyring, id, pref));
    if (result.success()) {
      Pointer p = pref.getValue();
      GKAttributeList gkal = new GKAttributeList(p);
      if (gkal.len > 0) {
        GKAttributeStruct attrib = new GKAttributeStruct(gkal.data);
        GKAttributeStruct[] attribArray = (GKAttributeStruct[]) attrib.toArray(gkal.len);
        for (GKAttributeStruct gka : attribArray)
          attributes.add(new Attribute<Object>(gka.name, gka.getValue()));
        gklib.gnome_keyring_attribute_list_free(p);
        return attributes;
      } else {
        return Collections.emptySet();
      }
    } else {
      return result.error();
    }
  }
}
