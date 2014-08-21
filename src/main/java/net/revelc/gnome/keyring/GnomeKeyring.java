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
import net.revelc.gnome.keyring.glib2.GArray;
import net.revelc.gnome.keyring.glib2.GList;
import net.revelc.gnome.keyring.glib2.GLib2;
import net.revelc.gnome.keyring.lib.GKAttributeStruct;
import net.revelc.gnome.keyring.lib.GKItemType;
import net.revelc.gnome.keyring.lib.GKLib;
import net.revelc.gnome.keyring.lib.GKResult;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import  com.sun.jna.NativeMappedConverter;

/**
 * 
 */
public class GnomeKeyring {

  private final GLib2 glib2;
  private final GKLib gklib;

  /**
   * Construct an interface to the underlying gnome-keyring library using the specified application name.
   * 
   * @param applicationName
   *          the name of the current GNU application
   */
  public GnomeKeyring(String applicationName) {
    glib2 = (GLib2) Native.loadLibrary(GLib2.NAME, GLib2.class);
    gklib = (GKLib) Native.loadLibrary(GKLib.NAME, GKLib.class);
    if (applicationName != null && !applicationName.isEmpty())
      glib2.g_set_application_name(applicationName);
  }

  /**
   * Retrieves a single item, identified by the specified keyring and id. Optionally include the stored secret.
   * 
   * @param keyring
   *          the name of the keyring to access
   * @param id
   *          the unique id representing a particular item in the specified keyring
   * @param includeSecret
   *          includes the secret in the returned data, if true
   * @return a representation of the requested item, whose stored secret can be destroyed by calling {@link GnomeKeyringItem#destroy()}
   * @throws GnomeKeyringException
   *           if the item doesn't exist or there is an error retrieving it
   */
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

  /**
   * Stores a network password with the given attributes in the specified keyring, and returns the id.
   * 
   * @param keyring
   *          the keyring in which to store
   * @param user
   *          the user name attribute
   * @param domain
   *          the application-specific domain attribute
   * @param server
   *          the host string attribute
   * @param path
   *          the path to the network object attribute
   * @param protocol
   *          the network protocol attribute
   * @param authtype
   *          the authentication type attribute
   * @param port
   *          optional port number (use 0) attribute
   * @param password
   *          the password to store
   * @return the id of the stored network password
   * @throws GnomeKeyringException
   *           if an error occurred
   */
  public int setNetworkPassword(String keyring, String user, String domain, String server, String path, String protocol, String authtype, int port,
      String password) throws GnomeKeyringException {
    IntByReference item_id_ref = new IntByReference();
    GKResult result = new GKResult(gklib, gklib.gnome_keyring_set_network_password_sync(keyring, user, domain, server, path, protocol, authtype, port,
        password, item_id_ref));
    if (result.success())
      return item_id_ref.getValue();
    else
      return result.error();
  }

  /**
   * Retrieves the name of the default keyring to access
   * 
   * @return the name of the current default keyring; usually "login" unless changed
   * @throws GnomeKeyringException
   *           if the name cannot be retrieved
   */
  public String getDefaultKeyring() throws GnomeKeyringException {
    PointerByReference keyring_ref = new PointerByReference();
    GKResult result = new GKResult(gklib, gklib.gnome_keyring_get_default_keyring_sync(keyring_ref));
    if (result.success()) {
      return keyring_ref.getValue().getString(0);
    } else {
      result.error();
      return null;
    }
  }

  /**
   * Retrieves the name of the session (non-persistent) keyring
   * 
   * @return the reserved keyword that refers to the session keyring
   */
  public String getSessionKeyring() throws GnomeKeyringException {
    return "session";
  }

  private Set<Attribute<?>> getItemAttributes(String keyring, int id) throws GnomeKeyringException {
    Set<Attribute<?>> attributes = new TreeSet<Attribute<?>>();
    PointerByReference pref = new PointerByReference();
    GKResult result = new GKResult(gklib, gklib.gnome_keyring_item_get_attributes_sync(keyring, id, pref));
    if (result.success()) {
      Pointer p = pref.getValue();
      GArray gkal = new GArray(p);
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

  public Set<Integer> getIds(String keyring) {
    Set<Integer> ids = new TreeSet<Integer>();

    PointerByReference pref = new PointerByReference();
    GKResult result = new GKResult(gklib, gklib.gnome_keyring_list_item_ids_sync(keyring, pref));
    if (result.success()) {
      Pointer p = pref.getValue();
      GList gkal = new GList(p);
      while(true) {
        long id = Pointer.nativeValue(gkal.data);
        ids.add((int)id);
        if(gkal.next != Pointer.NULL) {
            gkal = new GList(gkal.next);
        } else {
            break;
        }
    }
      return ids;
    }

    return Collections.emptySet();
  }
}
