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

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

/**
 * 
 */
public class GnomeKeyringAttribute extends Structure implements Structure.ByReference {

  public static enum GnomeKeyringAttributeType {
    GNOME_KEYRING_ATTRIBUTE_TYPE_STRING(0), GNOME_KEYRING_ATTRIBUTE_TYPE_UINT32(1);
    private int val;

    private GnomeKeyringAttributeType(int val) {
      this.val = val;
    }

    public static GnomeKeyringAttributeType fromValue(int val) {
      if (val >= 0) {
        for (GnomeKeyringAttributeType t : GnomeKeyringAttributeType.values()) {
          if (t.val == val)
            return t;
        }
      }
      throw new IllegalArgumentException("Unrecognized ordinal: " + val);
    }
  }

  public static class GnomeKeyringAttributeValue extends Union {
    public String string;
    public Integer integer;
  }

  public String name;
  public int type;
  public GnomeKeyringAttributeValue value;

  public GnomeKeyringAttribute(Pointer p) {
    super(p);
    read();
  }

  public String getValue() {
    GnomeKeyringAttributeType t = GnomeKeyringAttributeType.fromValue(type);
    switch (t) {
      case GNOME_KEYRING_ATTRIBUTE_TYPE_STRING:
        return (String) value.getTypedValue(String.class);
      case GNOME_KEYRING_ATTRIBUTE_TYPE_UINT32:
        return Integer.toString((Integer) value.getTypedValue(Integer.class));
    }
    return "";
  }

  @Override
  protected List<?> getFieldOrder() {
    return Arrays.asList(new String[] {"name", "type", "value"});
  }

}
