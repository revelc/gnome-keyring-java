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
import java.util.Set;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

/**
 * 
 */
public class GnomeKeyringItem implements Destroyable, Comparable<GnomeKeyringItem> {

  public static class Attribute<T> implements Comparable<Attribute<?>> {

    private String name;
    private T value;

    Attribute(String name, T value) {
      if (name == null || value == null)
        throw new IllegalArgumentException("Name and Value must not be null");
      this.name = name;
      this.value = value;
    }

    @Override
    public int compareTo(Attribute<?> o) {
      return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof Attribute))
        return false;
      Attribute<?> other = (Attribute<?>) obj;
      return (name.equals(other.name) && value.getClass().equals(other.value.getClass()) && value.equals(other.value));
    }

    public String getName() {
      return name;
    }

    public T getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return name.hashCode() + value.hashCode();
    }
  }

  private static boolean fieldsEqual(Object a, Object b) {
    if (a == null || b == null)
      return a == null && b == null;
    else
      return a == b || a.equals(b);
  }

  private String type, displayName = "", secret;

  private Date ctime, mtime;

  private Set<Attribute<?>> attributes;

  GnomeKeyringItem(String type, String displayName, String secret, Date ctime, Date mtime, Set<Attribute<?>> attributes) {
    this.type = type;
    this.displayName = displayName;
    this.secret = secret;
    this.ctime = ctime;
    this.mtime = mtime;
    this.attributes = attributes;
  }

  @Override
  public int compareTo(GnomeKeyringItem o) {
    if (o == null)
      return -1;
    if (getDisplayName() == null)
      return o.getDisplayName() == null ? 0 : 1;
    return getDisplayName().compareTo(o.getDisplayName());
  }

  @Override
  public void destroy() throws DestroyFailedException {
    if (secret != null) {
      secret = null;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof GnomeKeyringItem))
      return false;
    GnomeKeyringItem o = (GnomeKeyringItem) obj;
    return fieldsEqual(displayName, o.displayName) && fieldsEqual(type, o.type) && fieldsEqual(secret, o.secret) && fieldsEqual(ctime, o.ctime)
        && fieldsEqual(mtime, o.mtime) && fieldsEqual(attributes, o.attributes);
  }

  public Set<Attribute<?>> getAttributes() {
    return attributes;
  }

  public Date getCTime() {
    return ctime;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Date getMTime() {
    return mtime;
  }

  public String getSecret() {
    return secret;
  }

  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return displayName.hashCode();
  }

  @Override
  public boolean isDestroyed() {
    return secret == null;
  }

  @Override
  public String toString() {
    return toString(true);
  }

  public String toString(boolean showSecret) {
    int keyLength = "DisplayName".length();
    for (Attribute<?> attrib : getAttributes())
      keyLength = Math.max(keyLength, "Attrib[]  ".length() + attrib.getName().length() + attrib.getValue().getClass().getSimpleName().length());

    String formatString = "%-" + keyLength + "s: %s\n";

    StringBuilder sb = new StringBuilder();
    sb.append(String.format(formatString, "Type", getType()));
    sb.append(String.format(formatString, "DisplayName", getDisplayName()));
    sb.append(String.format(formatString, "Secret", showSecret ? getSecret() : ("**** " + (isDestroyed() ? "DESTROYED" : "HIDDEN") + " ****")));
    sb.append(String.format(formatString, "CTime", getCTime()));
    sb.append(String.format(formatString, "MTime", getMTime()));
    for (Attribute<?> attrib : getAttributes())
      sb.append(String.format(formatString, "Attrib[" + attrib.getValue().getClass().getSimpleName() + "]:" + attrib.getName(), attrib.getValue()));
    return sb.toString();
  }

}
