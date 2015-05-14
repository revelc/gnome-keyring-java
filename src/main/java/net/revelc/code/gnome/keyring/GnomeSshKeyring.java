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

package net.revelc.code.gnome.keyring;

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
