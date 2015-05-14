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

public enum GKItemType {
  /* The item types */
  GNOME_KEYRING_ITEM_GENERIC_SECRET(0),

  GNOME_KEYRING_ITEM_NETWORK_PASSWORD(1),

  GNOME_KEYRING_ITEM_NOTE(2),

  GNOME_KEYRING_ITEM_CHAINED_KEYRING_PASSWORD(3),

  GNOME_KEYRING_ITEM_ENCRYPTION_KEY_PASSWORD(4),

  GNOME_KEYRING_ITEM_PK_STORAGE(0x100),

  /* Not used, remains here only for compatibility */
  GNOME_KEYRING_ITEM_LAST_TYPE(0x101);

  public static GKItemType fromValue(int val) {
    if (val >= 0) {
      for (GKItemType t : GKItemType.values()) {
        if (t.val == val)
          return t;
      }
    }
    throw new IllegalArgumentException("Unrecognized ordinal: " + val);
  }

  private int val;

  private GKItemType(int val) {
    this.val = val;
  }
}
