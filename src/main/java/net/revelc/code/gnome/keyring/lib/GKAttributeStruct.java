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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

public class GKAttributeStruct extends Structure {

  public enum AttributeType {
    GNOME_KEYRING_ATTRIBUTE_TYPE_STRING(0), GNOME_KEYRING_ATTRIBUTE_TYPE_UINT32(1);

    public static AttributeType fromValue(int val) {
      if (val >= 0) {
        for (AttributeType t : AttributeType.values()) {
          if (t.val == val)
            return t;
        }
      }
      throw new IllegalArgumentException("Unrecognized ordinal: " + val);
    }

    private int val;

    private AttributeType(int val) {
      this.val = val;
    }
  }

  @FieldOrder({"name", "type", "value"})
  public static class GnomeKeyringAttributeValue extends Union {
    public String string;
    public Integer integer;
  }

  public String name;
  public int type;
  public GnomeKeyringAttributeValue value;

  public GKAttributeStruct(Pointer p) {
    super(p);
    read();
  }

  public Object getValue() {
    AttributeType t = AttributeType.fromValue(type);
    switch (t) {
      case GNOME_KEYRING_ATTRIBUTE_TYPE_STRING:
        return value.getTypedValue(String.class);
      case GNOME_KEYRING_ATTRIBUTE_TYPE_UINT32:
        return value.getTypedValue(Integer.class);
    }
    return "";
  }

}
