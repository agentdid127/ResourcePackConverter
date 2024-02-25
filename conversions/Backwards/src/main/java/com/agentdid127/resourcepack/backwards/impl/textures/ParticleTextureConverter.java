package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
        Path imagePath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "particle" + File.separator);
        if (!imagePath.toFile().exists()) return;
        if (!imagePath.resolve("particles.png").toFile().exists()) return;

        int defaultW = 128, defaultH = 128;

        // Particles
        // TODO: unused variable? - sammwi
        boolean isLegacy = false;
        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.14") && to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
            defaultW = 256;
            defaultH = 256;
            File[] imageFiles = imagePath.toFile().listFiles();
            String fileName = "";
            for (File file : imageFiles) {
                if (file.getName().endsWith(".png")) {
                    fileName = file.getName();
                    break;
                }
            }

            ImageConverter iconvert = new ImageConverter(8, 8, imagePath.resolve(fileName));
            iconvert.newImage(256, 256);
            iconvert.addImage(imagePath.resolve("angry.png"), 8, 40);

            iconvert.addImage(imagePath.resolve("bubble.png"), 0, 16);

            iconvert.addImage(imagePath.resolve("bubble_pop_0.png"), 0, 131);

            iconvert.addImage(imagePath.resolve("bubble_pop_1.png"), 16, 131);

            iconvert.addImage(imagePath.resolve("bubble_pop_2.png"), 32, 131);

            iconvert.addImage(imagePath.resolve("bubble_pop_3.png"), 48, 131);

            iconvert.addImage(imagePath.resolve("bubble_pop_4.png"), 64, 131);

            iconvert.addImage(imagePath.resolve("critical_hit.png"), 8, 32);

            iconvert.addImage(imagePath.resolve("damage.png"), 24, 32);

            iconvert.addImage(imagePath.resolve("drip_fall.png"), 8, 56);

            iconvert.addImage(imagePath.resolve("drip_hang.png"), 0, 56);

            iconvert.addImage(imagePath.resolve("drip_land.png"), 16, 56);

            iconvert.addImage(imagePath.resolve("effect_0.png"), 0, 64);
            iconvert.addImage(imagePath.resolve("effect_1.png"), 8, 64);

            iconvert.addImage(imagePath.resolve("effect_2.png"), 16, 64);

            iconvert.addImage(imagePath.resolve("effect_3.png"), 24, 64);

            iconvert.addImage(imagePath.resolve("effect_4.png"), 32, 64);

            iconvert.addImage(imagePath.resolve("effect_5.png"), 40, 64);

            iconvert.addImage(imagePath.resolve("effect_6.png"), 48, 64);

            iconvert.addImage(imagePath.resolve("effect_7.png"), 56, 64);

            iconvert.addImage(imagePath.resolve("enchanted_hit.png"), 16, 32);

            iconvert.addImage(imagePath.resolve("flame.png"), 0, 24);

            iconvert.addImage(imagePath.resolve("flash.png"), 32, 16);

            iconvert.addImage(imagePath.resolve("generic_0.png"), 0, 0);
            iconvert.addImage(imagePath.resolve("generic_1.png"), 8, 0);
            iconvert.addImage(imagePath.resolve("generic_2.png"), 16, 0);
            iconvert.addImage(imagePath.resolve("generic_3.png"), 24, 0);
            iconvert.addImage(imagePath.resolve("generic_4.png"), 32, 0);
            iconvert.addImage(imagePath.resolve("generic_5.png"), 40, 0);
            iconvert.addImage(imagePath.resolve("generic_6.png"), 48, 0);
            iconvert.addImage(imagePath.resolve("generic_7.png"), 56, 0);

            iconvert.addImage(imagePath.resolve("glint.png"), 16, 40);

            iconvert.addImage(imagePath.resolve("glitter_0.png"), 0, 88);
            iconvert.addImage(imagePath.resolve("glitter_1.png"), 8, 88);
            iconvert.addImage(imagePath.resolve("glitter_2.png"), 16, 88);
            iconvert.addImage(imagePath.resolve("glitter_3.png"), 24, 88);
            iconvert.addImage(imagePath.resolve("glitter_4.png"), 32, 88);
            iconvert.addImage(imagePath.resolve("glitter_5.png"), 40, 88);
            iconvert.addImage(imagePath.resolve("glitter_6.png"), 48, 88);
            iconvert.addImage(imagePath.resolve("glitter_7.png"), 56, 88);

            iconvert.addImage(imagePath.resolve("heart.png"), 0, 40);

            iconvert.addImage(imagePath.resolve("lava.png"), 8, 24);
            iconvert.addImage(imagePath.resolve("nautilus.png"), 0, 104);

            iconvert.addImage(imagePath.resolve("note.png"), 0, 32);

            iconvert.addImage(imagePath.resolve("sga_a.png"), 8, 112);
            iconvert.addImage(imagePath.resolve("sga_b.png"), 16, 112);
            iconvert.addImage(imagePath.resolve("sga_c.png"), 24, 112);
            iconvert.addImage(imagePath.resolve("sga_d.png"), 32, 112);
            iconvert.addImage(imagePath.resolve("sga_e.png"), 40, 112);
            iconvert.addImage(imagePath.resolve("sga_f.png"), 48, 112);
            iconvert.addImage(imagePath.resolve("sga_g.png"), 56, 112);
            iconvert.addImage(imagePath.resolve("sga_h.png"), 64, 112);
            iconvert.addImage(imagePath.resolve("sga_i.png"), 72, 112);
            iconvert.addImage(imagePath.resolve("sga_j.png"), 80, 112);
            iconvert.addImage(imagePath.resolve("sga_k.png"), 88, 112);
            iconvert.addImage(imagePath.resolve("sga_l.png"), 96, 112);
            iconvert.addImage(imagePath.resolve("sga_m.png"), 104, 112);
            iconvert.addImage(imagePath.resolve("sga_n.png"), 112, 112);
            iconvert.addImage(imagePath.resolve("sga_o.png"), 120, 112);
            iconvert.addImage(imagePath.resolve("sga_p.png"), 0, 120);
            iconvert.addImage(imagePath.resolve("sga_q.png"), 8, 120);
            iconvert.addImage(imagePath.resolve("sga_r.png"), 16, 120);
            iconvert.addImage(imagePath.resolve("sga_s.png"), 24, 120);
            iconvert.addImage(imagePath.resolve("sga_t.png"), 32, 120);
            iconvert.addImage(imagePath.resolve("sga_u.png"), 40, 120);
            iconvert.addImage(imagePath.resolve("sga_v.png"), 48, 120);
            iconvert.addImage(imagePath.resolve("sga_w.png"), 56, 120);
            iconvert.addImage(imagePath.resolve("sga_x.png"), 64, 120);
            iconvert.addImage(imagePath.resolve("sga_y.png"), 72, 120);
            iconvert.addImage(imagePath.resolve("sga_z.png"), 80, 120);

            iconvert.addImage(imagePath.resolve("spark_0.png"), 0, 80);
            iconvert.addImage(imagePath.resolve("spark_1.png"), 8, 80);
            iconvert.addImage(imagePath.resolve("spark_2.png"), 16, 80);
            iconvert.addImage(imagePath.resolve("spark_3.png"), 24, 80);

            iconvert.addImage(imagePath.resolve("spark_4.png"), 32, 80);
            iconvert.addImage(imagePath.resolve("spark_5.png"), 40, 80);
            iconvert.addImage(imagePath.resolve("spark_6.png"), 48, 80);
            iconvert.addImage(imagePath.resolve("spark_7.png"), 56, 80);

            iconvert.addImage(imagePath.resolve("spell_0.png"), 0, 72);
            iconvert.addImage(imagePath.resolve("spell_1.png"), 8, 72);
            iconvert.addImage(imagePath.resolve("spell_2.png"), 16, 72);
            iconvert.addImage(imagePath.resolve("spell_3.png"), 24, 72);
            iconvert.addImage(imagePath.resolve("spell_4.png"), 32, 72);
            iconvert.addImage(imagePath.resolve("spell_5.png"), 40, 72);
            iconvert.addImage(imagePath.resolve("spell_6.png"), 48, 72);
            iconvert.addImage(imagePath.resolve("spell_7.png"), 56, 72);

            iconvert.addImage(imagePath.resolve("splash_0.png"), 24, 8);
            iconvert.addImage(imagePath.resolve("splash_1.png"), 32, 8);
            iconvert.addImage(imagePath.resolve("splash_2.png"), 40, 8);
            iconvert.addImage(imagePath.resolve("splash_3.png"), 48, 8);

            iconvert.addImage(pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "entity" + File.separator + "fishing_hook.png"), 8, 16);

            iconvert.store(imagePath.resolve("particles.png"));
        }
        
        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.12.2") && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            defaultW = 128;
            defaultH = 128;
            ImageConverter iconvert = new ImageConverter(defaultW, defaultH, imagePath.resolve("particles.png"));
            if (!iconvert.imageIsPowerOfTwo())
                return;
            iconvert.newImage(128, 128);
            iconvert.subImage(0, 0, 128, 128, 0, 0);
        }
    }
}
