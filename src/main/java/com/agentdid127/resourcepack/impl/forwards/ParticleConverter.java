package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.utilities.ImageConverter;
import com.agentdid127.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ParticleConverter extends Converter {

    int from, to;
    public ParticleConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    /**
     * Updates Particles
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path imagePath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "particle" + File.separator);
        if (!imagePath.toFile().exists()) return;
        if (!imagePath.resolve("particles.png").toFile().exists()) return;

        int defaultW = 128, defaultH = 128;
        ImageConverter iconvert = new ImageConverter(defaultW, defaultH, imagePath.resolve("particles.png"));
        //Particles
        boolean isLegacy = false;
        if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")) {

            iconvert.newImage(256, 256);
            iconvert.subImage(0, 0, 128, 128, 0, 0);
            isLegacy = iconvert.store();
        }
        if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.13.2") && to >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
            defaultW = 256;
            defaultH = 256;
            if (!isLegacy) {
                iconvert = new ImageConverter(defaultW, defaultH, imagePath.resolve("particles.png"));
            }
            else {
                iconvert.setImage(defaultW, defaultH);
            }

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 40, 16, 48, 0 ,0);
            iconvert.store(imagePath.resolve("angry.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 16, 8, 24, 0 ,0);
            iconvert.store(imagePath.resolve("bubble.png"));

            iconvert.newImage(16, 16);
            iconvert.subImage(0, 131, 16, 147, 0 ,0);
            iconvert.store(imagePath.resolve("bubble_pop_0.png"));

            iconvert.newImage(16, 16);
            iconvert.subImage(16, 131, 32, 147, 0 ,0);
            iconvert.store(imagePath.resolve("bubble_pop_1.png"));

            iconvert.newImage(16, 16);
            iconvert.subImage(32, 131, 48, 147, 0 ,0);
            iconvert.store(imagePath.resolve("bubble_pop_2.png"));

            iconvert.newImage(16, 16);
            iconvert.subImage(48, 131, 64, 147, 0 ,0);
            iconvert.store(imagePath.resolve("bubble_pop_3.png"));

            iconvert.newImage(16, 16);
            iconvert.subImage(64, 131, 80, 147, 0 ,0);
            iconvert.store(imagePath.resolve("bubble_pop_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 32, 16, 40, 0 ,0);
            iconvert.store(imagePath.resolve("critical_hit.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 32, 32, 40, 0 ,0);
            iconvert.store(imagePath.resolve("damage.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 56, 16, 64, 0 ,0);
            iconvert.store(imagePath.resolve("drip_fall.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 56, 8, 64, 0 ,0);
            iconvert.store(imagePath.resolve("drip_hang.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 56, 24, 64, 0 ,0);
            iconvert.store(imagePath.resolve("drip_land.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 64, 8, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 64, 16, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 64, 24, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 64, 32, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 64, 40, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 64, 48, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_5.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 64, 56, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_6.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 64, 64, 72, 0 ,0);
            iconvert.store(imagePath.resolve("effect_7.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 32, 24, 40, 0 ,0);
            iconvert.store(imagePath.resolve("enchanted_hit.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 24, 8, 32, 0 ,0);
            iconvert.store(imagePath.resolve("flame.png"));

            iconvert.newImage(iconvert.getWidth() / 4, iconvert.getHeight() / 4);
            iconvert.subImage(32, 16, 64, 48, 0 ,0);
            iconvert.store(imagePath.resolve("flash.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 0, 8, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 0, 16, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 0, 24, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 0, 32, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 0, 40, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 0, 48, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_5.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 0, 56, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_6.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 0, 64, 8, 0 ,0);
            iconvert.store(imagePath.resolve("generic_7.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 40, 24, 48, 0 ,0);
            iconvert.store(imagePath.resolve("glint.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 88, 8, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 88, 16, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 88, 24, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 88, 32, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 88, 40, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 88, 48, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_5.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 88, 56, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_6.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 88, 64, 96, 0 ,0);
            iconvert.store(imagePath.resolve("glitter_7.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 40, 8, 48, 0 ,0);
            iconvert.store(imagePath.resolve("heart.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 24, 16, 32, 0 ,0);
            iconvert.store(imagePath.resolve("lava.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 104, 8, 112, 0 ,0);
            iconvert.store(imagePath.resolve("nautilus.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 32, 8, 40, 0 ,0);
            iconvert.store(imagePath.resolve("note.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 112, 16, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_a.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 112, 24, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_b.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 112, 32, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_c.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 112, 40, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_d.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 112, 48, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_e.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 112, 56, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_f.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 112, 64, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_g.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(64, 112, 72, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_h.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(72, 112, 80, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_i.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(80, 112, 88, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_j.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(88, 112, 96, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_k.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(96, 112, 104, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_l.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(104, 112, 112, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_m.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(112, 112, 120, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_n.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(120, 112, 128, 120, 0 ,0);
            iconvert.store(imagePath.resolve("sga_o.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 120, 8, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_p.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 120, 16, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_q.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 120, 24, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_r.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 120, 32, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_s.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 120, 40, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_t.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 120, 48, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_u.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 120, 56, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_v.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 120, 64, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_w.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(64, 120, 72, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_x.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(72, 120, 80, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_y.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(80, 120, 88, 128, 0 ,0);
            iconvert.store(imagePath.resolve("sga_z.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 80, 8, 96, 0 ,0);
            iconvert.store(imagePath.resolve("spark_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 80, 16, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 80, 24, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 80, 32, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 80, 40, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 80, 48, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_5.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 80, 56, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_6.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 80, 64, 88, 0 ,0);
            iconvert.store(imagePath.resolve("spark_7.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(0, 72, 8, 96, 0 ,0);
            iconvert.store(imagePath.resolve("spell_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 72, 16, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(16, 72, 24, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 72, 32, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 72, 40, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_4.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 72, 48, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_5.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 72, 56, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_6.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(56, 72, 64, 80, 0 ,0);
            iconvert.store(imagePath.resolve("spell_7.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(24, 8, 32, 16, 0 ,0);
            iconvert.store(imagePath.resolve("splash_0.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(32, 8, 40, 16, 0 ,0);
            iconvert.store(imagePath.resolve("splash_1.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(40, 8, 48, 16, 0 ,0);
            iconvert.store(imagePath.resolve("splash_2.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(48, 8, 56, 16, 0 ,0);
            iconvert.store(imagePath.resolve("splash_3.png"));

            iconvert.newImage(8, 8);
            iconvert.subImage(8, 16, 16, 24, 0 ,0);
            iconvert.store(pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "entity" + File.separator + "fishing_hook.png"));

        }
    }
    }

