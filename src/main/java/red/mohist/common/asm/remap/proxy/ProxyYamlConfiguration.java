package red.mohist.common.asm.remap.proxy;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author pyz
 * @date 2019/7/9 10:36 AM
 */
public class ProxyYamlConfiguration {
    public static YamlConfiguration loadConfiguration(InputStream inputStream) {
        return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charset.defaultCharset()));
    }
}
