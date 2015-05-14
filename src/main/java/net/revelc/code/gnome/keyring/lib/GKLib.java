/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.revelc.code.gnome.keyring.lib;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface GKLib extends Library {

  static final String NAME = "gnome-keyring";

  void gnome_keyring_attribute_list_free(Pointer p);

  int gnome_keyring_item_get_attributes_sync(String keyring, int id, PointerByReference attributes);

  int gnome_keyring_item_get_info_full_sync(String keyring, int id, int flags, PointerByReference item_info);

  void gnome_keyring_item_info_free(Pointer item_info);

  long gnome_keyring_item_info_get_ctime(Pointer item_info);

  String gnome_keyring_item_info_get_display_name(Pointer item_info);

  long gnome_keyring_item_info_get_mtime(Pointer item_info);

  String gnome_keyring_item_info_get_secret(Pointer item_info);

  int gnome_keyring_item_info_get_type(Pointer item_info);

  Pointer gnome_keyring_item_info_new();

  String gnome_keyring_result_to_message(int r);

  int gnome_keyring_set_network_password_sync(String keyring, String user, String domain, String server, String object, String protocol, String authtype,
      int port, String password, IntByReference item_id);

  int gnome_keyring_get_default_keyring_sync(PointerByReference keyring);

  int gnome_keyring_list_item_ids_sync (String keyring, PointerByReference ids);

}
