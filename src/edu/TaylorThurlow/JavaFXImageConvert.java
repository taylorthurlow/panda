package edu.TaylorThurlow;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class JavaFXImageConvert
{

	public static Image getJavaFXImage(byte[] rawPixels, int width, int height)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			ImageIO.write((RenderedImage) createBufferedImage(rawPixels, width, height), "jpg", out);
			out.flush();
		} catch (IOException e)
		{
			musicPlayer.getInstance().showErrorDialog(e.getMessage());
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		return new javafx.scene.image.Image(in);
	}

	private static BufferedImage createBufferedImage(byte[] pixels, int width, int height)
	{
		SampleModel sm = getIndexSampleModel(width, height);
		DataBuffer db = new DataBufferByte(pixels, width * height, 0);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		IndexColorModel cm = getDefaultColorModel();
		BufferedImage image = new BufferedImage(cm, raster, false, null);
		return image;
	}

	private static SampleModel getIndexSampleModel(int width, int height)
	{
		IndexColorModel icm = getDefaultColorModel();
		WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
		SampleModel sampleModel = wr.getSampleModel();
		sampleModel = sampleModel.createCompatibleSampleModel(width, height);
		return sampleModel;

	}

	private static IndexColorModel getDefaultColorModel()
	{
		byte[] r = new byte[256];
		byte[] g = new byte[256];
		byte[] b = new byte[256];
		for (int i = 0; i < 256; i++)
		{
			r[i] = (byte) i;
			g[i] = (byte) i;
			b[i] = (byte) i;
		}
		IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
		return defaultColorModel;
	}

	public void findMindAndMax(short[] pixels, int width, int height)
	{
		int size = width * height;
		int value;
		int min = 65535;
		int max = 0;
		for (int i = 0; i < size; i++)
		{
			value = pixels[i] & 0xffff;
			if (value < min)
			{
				min = value;
			}
			if (value > max)
			{
				max = value;
			}
		}
	}
}
