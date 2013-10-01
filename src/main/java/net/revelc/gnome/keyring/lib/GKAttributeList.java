package net.revelc.gnome.keyring.lib;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class GKAttributeList extends Structure {

  public Pointer data;
  public int len;

  public GKAttributeList() {}

  public GKAttributeList(Pointer p) {
    super(p);
    read();
  }

  @Override
  protected List<?> getFieldOrder() {
    return Arrays.asList(new String[] {"data", "len"});
  }
}
