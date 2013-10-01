package net.revelc.gnome.keyring.lib;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class GnomeKeyringAttributeList extends Structure implements Structure.ByReference {

  public Pointer data;
  public int len;

  public GnomeKeyringAttributeList() {}

  public GnomeKeyringAttributeList(Pointer p) {
    super(p);
    read();
  }

  @Override
  protected List<?> getFieldOrder() {
    return Arrays.asList(new String[] {"data", "len"});
  }
}
