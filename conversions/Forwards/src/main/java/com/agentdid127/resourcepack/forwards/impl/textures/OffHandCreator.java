package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class OffHandCreator extends Converter {
	public OffHandCreator(PackConverter packConverter) {
		super(packConverter);
	}

	@Override
	public void convert(Pack pack) throws IOException {
		Path widgetsPath = pack.getWorkingPath()
				.resolve("assets/minecraft/textures/gui/widgets.png".replace("/", File.separator));
		if (!widgetsPath.toFile().exists())
			return;

		ImageConverter converter = new ImageConverter(256, 256, widgetsPath);

		int hotbar_end_x = 182;

		int hotbar_offhand_width = 29;
		int hotbar_offhand_height = 24;

		int hotbar_offhand_l_x = 24;
		int hotbar_offhand_r_x = 60;
		int hotbar_offhand_y = 22;

		int slice_w = 11;
		int slice_h = 22;

		converter.fillEmpty(hotbar_offhand_l_x, hotbar_offhand_y, hotbar_offhand_width, hotbar_offhand_height);
		converter.fillEmpty(hotbar_offhand_r_x, hotbar_offhand_y, hotbar_offhand_width, hotbar_offhand_height);

		// OffHand (Left)
		converter.subImage(
				0,
				0,
				slice_w,
				slice_h,
				hotbar_offhand_l_x,
				hotbar_offhand_y + 1);
		converter.subImage(
				hotbar_end_x - slice_w,
				0,
				hotbar_end_x,
				slice_h,
				hotbar_offhand_l_x + slice_w,
				hotbar_offhand_y + 1);

		// OffHand (Right)
		converter.subImage(
				0,
				0,
				slice_w,
				slice_h,
				hotbar_offhand_r_x,
				hotbar_offhand_y + 1);
		converter.subImage(
				hotbar_end_x - slice_w,
				0,
				hotbar_end_x,
				slice_h,
				hotbar_offhand_r_x + slice_w,
				hotbar_offhand_y + 1);

		converter.store();
	}
}
