package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class ParticleTextureConverter extends Converter {
    int from, to;

    public ParticleTextureConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    /**
     * Updates Particles
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        if (!texturesPath.toFile().exists())
            return;

        Path particleFolderPath = texturesPath.resolve("particle");
        if (!particleFolderPath.toFile().exists())
            return;

        Path particlesImagePath = particleFolderPath.resolve("particles.png");
        if (!particlesImagePath.toFile().exists())
            return;

        int defaultW = 128, defaultH = 128;

        // Particles
        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.14")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
            defaultW = 256;
            defaultH = 256;

            BufferedImage image = new BufferedImage(defaultW, defaultH, BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(image, "png", particlesImagePath.toFile());

            ImageConverter converter = new ImageConverter(8, 8, particlesImagePath);
            converter.newImage(256, 256);
            converter.addImage(particleFolderPath.resolve("angry.png"), 8, 40);
            converter.addImage(particleFolderPath.resolve("bubble.png"), 0, 16);
            converter.addImage(particleFolderPath.resolve("bubble_pop_0.png"), 0, 131);
            converter.addImage(particleFolderPath.resolve("bubble_pop_1.png"), 16, 131);
            converter.addImage(particleFolderPath.resolve("bubble_pop_2.png"), 32, 131);
            converter.addImage(particleFolderPath.resolve("bubble_pop_3.png"), 48, 131);
            converter.addImage(particleFolderPath.resolve("bubble_pop_4.png"), 64, 131);
            converter.addImage(particleFolderPath.resolve("critical_hit.png"), 8, 32);
            converter.addImage(particleFolderPath.resolve("damage.png"), 24, 32);
            converter.addImage(particleFolderPath.resolve("drip_fall.png"), 8, 56);
            converter.addImage(particleFolderPath.resolve("drip_hang.png"), 0, 56);
            converter.addImage(particleFolderPath.resolve("drip_land.png"), 16, 56);
            converter.addImage(particleFolderPath.resolve("effect_0.png"), 0, 64);
            converter.addImage(particleFolderPath.resolve("effect_1.png"), 8, 64);
            converter.addImage(particleFolderPath.resolve("effect_2.png"), 16, 64);
            converter.addImage(particleFolderPath.resolve("effect_3.png"), 24, 64);
            converter.addImage(particleFolderPath.resolve("effect_4.png"), 32, 64);
            converter.addImage(particleFolderPath.resolve("effect_5.png"), 40, 64);
            converter.addImage(particleFolderPath.resolve("effect_6.png"), 48, 64);
            converter.addImage(particleFolderPath.resolve("effect_7.png"), 56, 64);
            converter.addImage(particleFolderPath.resolve("enchanted_hit.png"), 16, 32);
            converter.addImage(particleFolderPath.resolve("flame.png"), 0, 24);
            converter.addImage(particleFolderPath.resolve("flash.png"), 32, 16);
            converter.addImage(particleFolderPath.resolve("generic_0.png"), 0, 0);
            converter.addImage(particleFolderPath.resolve("generic_1.png"), 8, 0);
            converter.addImage(particleFolderPath.resolve("generic_2.png"), 16, 0);
            converter.addImage(particleFolderPath.resolve("generic_3.png"), 24, 0);
            converter.addImage(particleFolderPath.resolve("generic_4.png"), 32, 0);
            converter.addImage(particleFolderPath.resolve("generic_5.png"), 40, 0);
            converter.addImage(particleFolderPath.resolve("generic_6.png"), 48, 0);
            converter.addImage(particleFolderPath.resolve("generic_7.png"), 56, 0);
            converter.addImage(particleFolderPath.resolve("glint.png"), 16, 40);
            converter.addImage(particleFolderPath.resolve("glitter_0.png"), 0, 88);
            converter.addImage(particleFolderPath.resolve("glitter_1.png"), 8, 88);
            converter.addImage(particleFolderPath.resolve("glitter_2.png"), 16, 88);
            converter.addImage(particleFolderPath.resolve("glitter_3.png"), 24, 88);
            converter.addImage(particleFolderPath.resolve("glitter_4.png"), 32, 88);
            converter.addImage(particleFolderPath.resolve("glitter_5.png"), 40, 88);
            converter.addImage(particleFolderPath.resolve("glitter_6.png"), 48, 88);
            converter.addImage(particleFolderPath.resolve("glitter_7.png"), 56, 88);
            converter.addImage(particleFolderPath.resolve("heart.png"), 0, 40);
            converter.addImage(particleFolderPath.resolve("lava.png"), 8, 24);
            converter.addImage(particleFolderPath.resolve("nautilus.png"), 0, 104);
            converter.addImage(particleFolderPath.resolve("note.png"), 0, 32);
            converter.addImage(particleFolderPath.resolve("sga_a.png"), 8, 112);
            converter.addImage(particleFolderPath.resolve("sga_b.png"), 16, 112);
            converter.addImage(particleFolderPath.resolve("sga_c.png"), 24, 112);
            converter.addImage(particleFolderPath.resolve("sga_d.png"), 32, 112);
            converter.addImage(particleFolderPath.resolve("sga_e.png"), 40, 112);
            converter.addImage(particleFolderPath.resolve("sga_f.png"), 48, 112);
            converter.addImage(particleFolderPath.resolve("sga_g.png"), 56, 112);
            converter.addImage(particleFolderPath.resolve("sga_h.png"), 64, 112);
            converter.addImage(particleFolderPath.resolve("sga_i.png"), 72, 112);
            converter.addImage(particleFolderPath.resolve("sga_j.png"), 80, 112);
            converter.addImage(particleFolderPath.resolve("sga_k.png"), 88, 112);
            converter.addImage(particleFolderPath.resolve("sga_l.png"), 96, 112);
            converter.addImage(particleFolderPath.resolve("sga_m.png"), 104, 112);
            converter.addImage(particleFolderPath.resolve("sga_n.png"), 112, 112);
            converter.addImage(particleFolderPath.resolve("sga_o.png"), 120, 112);
            converter.addImage(particleFolderPath.resolve("sga_p.png"), 0, 120);
            converter.addImage(particleFolderPath.resolve("sga_q.png"), 8, 120);
            converter.addImage(particleFolderPath.resolve("sga_r.png"), 16, 120);
            converter.addImage(particleFolderPath.resolve("sga_s.png"), 24, 120);
            converter.addImage(particleFolderPath.resolve("sga_t.png"), 32, 120);
            converter.addImage(particleFolderPath.resolve("sga_u.png"), 40, 120);
            converter.addImage(particleFolderPath.resolve("sga_v.png"), 48, 120);
            converter.addImage(particleFolderPath.resolve("sga_w.png"), 56, 120);
            converter.addImage(particleFolderPath.resolve("sga_x.png"), 64, 120);
            converter.addImage(particleFolderPath.resolve("sga_y.png"), 72, 120);
            converter.addImage(particleFolderPath.resolve("sga_z.png"), 80, 120);
            converter.addImage(particleFolderPath.resolve("spark_0.png"), 0, 80);
            converter.addImage(particleFolderPath.resolve("spark_1.png"), 8, 80);
            converter.addImage(particleFolderPath.resolve("spark_2.png"), 16, 80);
            converter.addImage(particleFolderPath.resolve("spark_3.png"), 24, 80);
            converter.addImage(particleFolderPath.resolve("spark_4.png"), 32, 80);
            converter.addImage(particleFolderPath.resolve("spark_5.png"), 40, 80);
            converter.addImage(particleFolderPath.resolve("spark_6.png"), 48, 80);
            converter.addImage(particleFolderPath.resolve("spark_7.png"), 56, 80);
            converter.addImage(particleFolderPath.resolve("spell_0.png"), 0, 72);
            converter.addImage(particleFolderPath.resolve("spell_1.png"), 8, 72);
            converter.addImage(particleFolderPath.resolve("spell_2.png"), 16, 72);
            converter.addImage(particleFolderPath.resolve("spell_3.png"), 24, 72);
            converter.addImage(particleFolderPath.resolve("spell_4.png"), 32, 72);
            converter.addImage(particleFolderPath.resolve("spell_5.png"), 40, 72);
            converter.addImage(particleFolderPath.resolve("spell_6.png"), 48, 72);
            converter.addImage(particleFolderPath.resolve("spell_7.png"), 56, 72);
            converter.addImage(particleFolderPath.resolve("splash_0.png"), 24, 8);
            converter.addImage(particleFolderPath.resolve("splash_1.png"), 32, 8);
            converter.addImage(particleFolderPath.resolve("splash_2.png"), 40, 8);
            converter.addImage(particleFolderPath.resolve("splash_3.png"), 48, 8);

            Path entityPath = texturesPath.resolve("entity");
            Path fishingHookPath = entityPath.resolve("fishing_hook.png");
            if (fishingHookPath.toFile().exists())
                converter.addImage(fishingHookPath, 8, 16);

            converter.store();
        }

        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            defaultW = 128;
            defaultH = 128;
            ImageConverter converter = new ImageConverter(defaultW, defaultH, particlesImagePath);
            if (!converter.fileIsPowerOfTwo())
                return;
            converter.newImage(128, 128);
            converter.subImage(0, 0, 128, 128, 0, 0);
            converter.store();
        }
    }
}
