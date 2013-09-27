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
package net.revelc.gnome.keyring.lib;

/**
 * 
 */
public enum GnomeKeyringItemType {
  UNKNOWN_ITEM_TYPE(-1),

  /* The item types */
  GNOME_KEYRING_ITEM_GENERIC_SECRET(0),

  GNOME_KEYRING_ITEM_NETWORK_PASSWORD(1),

  GNOME_KEYRING_ITEM_NOTE(2),

  GNOME_KEYRING_ITEM_CHAINED_KEYRING_PASSWORD(3),

  GNOME_KEYRING_ITEM_ENCRYPTION_KEY_PASSWORD(4),

  GNOME_KEYRING_ITEM_PK_STORAGE(0x100),

  /* Not used, remains here only for compatibility */
  GNOME_KEYRING_ITEM_LAST_TYPE(0x101);

  private int val;

  private GnomeKeyringItemType(int val) {
    this.val = val;
  }

  public static GnomeKeyringItemType fromValue(int val) {
    if (val >= 0) {
      for (GnomeKeyringItemType t : GnomeKeyringItemType.values()) {
        if (t.val == val)
          return t;
      }
    }
    return UNKNOWN_ITEM_TYPE;
  }
}
