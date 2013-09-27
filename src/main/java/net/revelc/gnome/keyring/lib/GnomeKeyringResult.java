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
public enum GnomeKeyringResult {
  UNKNOWN_RESULT_TYPE(-1),
  GNOME_KEYRING_RESULT_OK(0),
  GNOME_KEYRING_RESULT_DENIED(1),
  GNOME_KEYRING_RESULT_NO_KEYRING_DAEMON(2),
  GNOME_KEYRING_RESULT_ALREADY_UNLOCKED(3),
  GNOME_KEYRING_RESULT_NO_SUCH_KEYRING(4),
  GNOME_KEYRING_RESULT_BAD_ARGUMENTS(5),
  GNOME_KEYRING_RESULT_IO_ERROR(6),
  GNOME_KEYRING_RESULT_CANCELLED(7),
  GNOME_KEYRING_RESULT_KEYRING_ALREADY_EXISTS(8),
  GNOME_KEYRING_RESULT_NO_MATCH(9);

  private int val;

  private GnomeKeyringResult(int val) {
    this.val = val;
  }

  public static GnomeKeyringResult fromValue(int val) {
    if (val >= 0) {
      for (GnomeKeyringResult t : GnomeKeyringResult.values()) {
        if (t.val == val)
          return t;
      }
    }
    return UNKNOWN_RESULT_TYPE;
  }
}
