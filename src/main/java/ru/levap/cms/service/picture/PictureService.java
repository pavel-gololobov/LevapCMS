package ru.levap.cms.service.picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import lombok.extern.slf4j.Slf4j;
import ru.levap.cms.persistance.model.FolderParameter;

@Slf4j
@Service
public class PictureService {
	
	@Autowired
	private PictureProperties pictureProperties;

	public List<FolderParameter> createImageThumbnail(Path inFile, PictureSizeType sizeType, boolean readExif) {
		BufferedImage original = null;
		List<FolderParameter> params = new ArrayList<>();
		try {
			original = ImageIO.read(inFile.toFile());
			if(readExif) {
				Metadata metadata = ImageMetadataReader.readMetadata(inFile.toFile());
				for (Directory directory : metadata.getDirectories()) {
		            for (Tag tag : directory.getTags()) {
		            	if(tag.getTagName() != null && tag.getDescription() != null && tag.getDescription().length() < 1000) {
			            	FolderParameter p = new FolderParameter();
			            	p.setName(tag.getTagName());
			            	p.setValue(tag.getDescription());
			            	params.add(p);
		            	}
		            }
		            for (String error : directory.getErrors()) {
		                log.error("Exif error {}", error);
		            }
		        }
			}
		} catch (IOException e) {
		    log.error("load image error {}", inFile, e);
		    return params;
		} catch (ImageProcessingException e) {
			log.error("load image exif error {}", inFile, e);
		    return params;
		}
		
		int targetSize = 300;
		switch (sizeType) {
		case THUMBNAIL:
			targetSize = pictureProperties.getThumbSize();
			break;
		case SMALL:
			targetSize = pictureProperties.getSmallSize();
			break;
		case MEDIUM:
			targetSize = pictureProperties.getMediumSize();
			break;
		case LARGE:
			targetSize = pictureProperties.getLargeSize();
			break;
		default:
			break;
		}
		BufferedImage scaledImage = Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, targetSize);
		String fileName = FilenameUtils.getBaseName(inFile.toString());
		String outPath = FilenameUtils.getFullPath(inFile.toString()) + fileName + "_" + sizeType.toString().toLowerCase() + ".jpg";
		
		try {
			ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.97F);

			FileImageOutputStream output = new FileImageOutputStream(new File(outPath));
			writer.setOutput(output);
			IIOImage image = new IIOImage(scaledImage, null, null);
			writer.write(null, image, param);
		} catch (IOException e) {
		    log.error("save image error {}", e);
		}
		
		return params;
	}
}
