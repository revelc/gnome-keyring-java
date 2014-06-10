
package net.revelc.gnome.keyring;

import java.util.HashMap;

class GnomeSshKeyring {
    private HashMap<String, String> cache;
    // = new HashMap<String, String>(); 
    public GnomeSshKeyring() {
        loadData();
    }

    public String getPassPhrase(String filename) {
        return cache.get(filename);
    }

    private void loadData() {
        try {

        GnomeKeyring gk = new GnomeKeyring("Java Example");
        String keyring = gk.getDefaultKeyring();

        cache = new HashMap<String, String>(); 

        for(Integer i: gk.getIds(keyring)) {
            GnomeKeyringItem item = gk.getItem(keyring, i, false);

            for (GnomeKeyringItem.Attribute<?> attrib : item.getAttributes()){
                if (attrib.getName().equals("unique")) {
                    String value = (String)attrib.getValue();
                    if (value.startsWith("ssh-store:")) {
                        GnomeKeyringItem SecretItem = gk.getItem(keyring, i, true);
                        cache.put(value.substring(10), SecretItem.getSecret());

                    }
                }
            }
        }
        } catch (GnomeKeyringException ex) {

        }
    }
}
