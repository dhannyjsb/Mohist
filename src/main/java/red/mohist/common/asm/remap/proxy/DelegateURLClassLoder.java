package red.mohist.common.asm.remap.proxy;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import red.mohist.common.asm.remap.RemapUtils;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author pyz
 * @date 2019/7/1 8:41 PM
 */
public class DelegateURLClassLoder extends URLClassLoader {

    public static final String desc = DelegateURLClassLoder.class.getName().replace('.', '/');
    private final PluginDescriptionFile description;

    {
        PluginDescriptionFile description = null;
        Class curClass = this.getClass();
        ClassLoader classLoader = curClass.getClassLoader();
        while (true) {
            if (classLoader == null) {
                break;
            }
            if (classLoader instanceof PluginClassLoader) {
                description = ((PluginClassLoader) classLoader).getDescription();
                break;
            }
            classLoader = classLoader.getClass().getClassLoader();
        }
        this.description = description;
    }

    private final Map<String, Class<?>> classeCache = new HashMap<>();

    public DelegateURLClassLoder(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public DelegateURLClassLoder(final URL[] urls) {
        super(urls);
    }

    public DelegateURLClassLoder(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (name.startsWith(RemapUtils.nmsPrefix) || name.startsWith(RemapUtils.mohistPrefix)) {
            String mapName = RemapUtils.map(name.replace('.', '/')).replace('/', '.');
            return JavaPlugin.class.getClassLoader().loadClass(mapName);
        }
        Class<?> result = this.classeCache.get(name);
        if (result != null) {
            return result;
        }
        synchronized (name.intern()) {
            result = this.classeCache.get(name);
            if (result != null) {
                return result;
            }
            result = this.remappedFindClass(name);
            if (result == null) {
                try {
                    result = super.findClass(name);
                } catch (ClassNotFoundException e) {
                    result = ((LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader()).findClass(name);
                }
            }
            if (result == null) {
                throw new ClassNotFoundException(name);
            }
            this.cacheClass(name, result);
        }
        return result;
    }

    protected Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            final String path = name.replace('.', '/').concat(".class");
            final URL url = this.findResource(path);
            if (url == null) {
                return null;
            }
            final InputStream stream = url.openStream();
            if (stream == null) {
                return null;
            }
            byte[] bytecode = IOUtils.toByteArray(stream);
            bytecode = RemapUtils.remapFindClass(description, name, bytecode);
            final JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            final URL jarURL = jarURLConnection.getJarFileURL();
            final CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
            result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
            if (result != null) {
                this.resolveClass(result);
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }
        return result;
    }

    protected void cacheClass(final String name, final Class<?> clazz) {
        this.classeCache.put(name, clazz);
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            ConfigurationSerialization.registerClass((Class<? extends ConfigurationSerializable>) clazz);
        }
    }
}