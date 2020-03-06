package ru.levap.cms.service.folder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.levap.cms.dto.common.BreadcrumbsNodeType;
import ru.levap.cms.dto.common.ErrorType;
import ru.levap.cms.dto.mapper.FolderMapper;
import ru.levap.cms.dto.web.BreadcrumbsDTO;
import ru.levap.cms.dto.web.ErrorDTO;
import ru.levap.cms.dto.web.FolderDTO;
import ru.levap.cms.dto.web.FolderListDTO;
import ru.levap.cms.dto.web.FolderParameterDTO;
import ru.levap.cms.dto.web.PageDTO;
import ru.levap.cms.exceptions.CommonException;
import ru.levap.cms.persistance.model.Folder;
import ru.levap.cms.persistance.model.FolderParameter;
import ru.levap.cms.persistance.repository.FolderParameterRepository;
import ru.levap.cms.persistance.repository.FolderRepository;
import ru.levap.cms.utils.DateTimeUtils;

@Service
public class FolderService {
	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private FolderParameterRepository folderParameterRepository;
	
	@Autowired
	private FolderProperties folderProperties;
	
	@Transactional
	public void saveFolderParameters(List<FolderParameter> params) {
		folderParameterRepository.saveAll(params);
	}
	
	public FolderDTO getOneFolderWithExif(Folder folder, boolean isStream) {
		List<String> exifParamsToView = new ArrayList<>();
		exifParamsToView.add("Model");
		exifParamsToView.add("Lens");
		exifParamsToView.add("Focal Length 35");
		exifParamsToView.add("Software");
		exifParamsToView.add("Date/Time");
		exifParamsToView.add("Exposure Time");
		exifParamsToView.add("F-Number");
		exifParamsToView.add("ISO");
		exifParamsToView.add("Exposure Program");
		exifParamsToView.add("Exposure Mode");
		
		FolderDTO folderDTO = FolderMapper.toDTO(folder);
		folderDTO.setStream(isStream);
		
		Sort sortPrev = Sort.by(Sort.Order.asc("createDate"));
		Sort sortNext = Sort.by(Sort.Order.desc("createDate"));
		Page<Folder> prev = null;
		Page<Folder> next= null;
		
		if(isStream) {
			next = folderRepository.findByActiveAndPictureNotNullAndCreateDateLessThen(true, folder.getCreateDate(), PageRequest.of(0, 2, sortNext));
			prev = folderRepository.findByActiveAndPictureNotNullAndCreateDateMoreThen(true, folder.getCreateDate(), PageRequest.of(0, 2, sortPrev));
		} else {
			next = folderRepository.findByActiveAndParentAndPictureNotNullAndCreateDateLessThen(true, folder.getParent(), folder.getCreateDate(), PageRequest.of(0, 2, sortNext));
			prev = folderRepository.findByActiveAndParentAndPictureNotNullAndCreateDateMoreThen(true, folder.getParent(), folder.getCreateDate(), PageRequest.of(0, 2, sortPrev));
		}

		if(prev.getContent().size() > 1) {
			folderDTO.setPreviousId(prev.getContent().get(1).getId());
		}
		if(next.getContent().size() > 1) {
			folderDTO.setNextId(next.getContent().get(1).getId());
		}

		for(String paramName : exifParamsToView) {
			FolderParameter fp = folder.getParameters().get(paramName);
			if(fp != null) {
				folderDTO.getParameters().add(FolderParameterDTO.builder().name(paramName).value(fp.getValue()).build());
			} else {
				folderDTO.getParameters().add(FolderParameterDTO.builder().name(paramName).value("-").build());
			}
		}
		return folderDTO;
	}
	
	public FolderListDTO getStream(Principal principal, Integer page) {
		FolderListDTO foldersDTO = FolderListDTO.builder() //
				.folders(new ArrayList<>()).build();
		Page<Folder> folders = null;
		
		Sort sort = Sort.by(Sort.Order.desc("createDate"));
		folders = folderRepository.findByActiveAndPictureNotNull(true, PageRequest.of(page, folderProperties.getPageSize(), sort));
		
		for(Folder f : folders) {
			FolderDTO fDTO = FolderMapper.toDTO(f);
			if(principal == null) {
				fDTO.setPosition(null);
			}
			foldersDTO.getFolders().add(fDTO);
		}
		
		PageDTO pDTO = new PageDTO(folders.getNumber(), folders.getTotalPages());
		foldersDTO.setPage(pDTO);
		return foldersDTO;
	}

	public FolderListDTO getFolders(Folder parentFolder, Principal principal, Integer page, boolean usePosition) {
		FolderListDTO foldersDTO = FolderListDTO.builder() //
				.folders(new ArrayList<>()).build();
		Page<Folder> folders = null;
		
		Sort sort = null;
		if(usePosition) {
			sort = Sort.by(
			    Sort.Order.desc("createDate"),
			    Sort.Order.desc("position"));
		} else {
			sort = Sort.by(
				    Sort.Order.desc("createDate"));
		}

		if(principal == null) {
			folders = folderRepository.findByActiveAndParent(true, parentFolder, PageRequest.of(page, folderProperties.getPageSize(), sort));
		} else {
			
			folders = folderRepository.findByParent(parentFolder, PageRequest.of(page, folderProperties.getPageSize(), sort));
		}
		for(Folder f : folders) {
			FolderDTO fDTO = FolderMapper.toDTO(f);
			if(principal == null) {
				fDTO.setPosition(null);
			}
			foldersDTO.getFolders().add(fDTO);
		}
		PageDTO pDTO = new PageDTO(page, (folders.getNumberOfElements() % folderProperties.getPageSize() > 0) ? 
				(folders.getNumberOfElements() / folderProperties.getPageSize() + 1) : folders.getNumberOfElements() / folderProperties.getPageSize());
		foldersDTO.setPage(pDTO);
		return foldersDTO;
	}
	
	@Transactional
	public Folder createPicture(String name, Folder parent) {
		Folder f = new Folder();
		f.setActive(true);
		f.setCreateDate(DateTimeUtils.getLongTimeNow());
		f.setDescription("");
		f.setName(name);
		f.setParent(parent);
		f.setPicture(name);
		f.setUpdateDate(DateTimeUtils.getLongTimeNow());
		folderRepository.save(f);
		return f;
	}

	@Transactional
	public ErrorDTO createFolder(FolderDTO folderDTO, Principal principal) {
		if((folderDTO.getName() == null) || folderDTO.getName().isEmpty()) {
			throw new CommonException(HttpStatus.BAD_REQUEST, "Bad folder name");
		}
		Folder parentFolder = null;
		if(folderDTO.getParentId() != null) {
			Optional<Folder> parentFolderOpt = folderRepository.findById(folderDTO.getParentId());
			if(parentFolderOpt.isPresent()) {
				parentFolder = parentFolderOpt.get();
			}
		}
		Folder f = new Folder();
		f.setActive(true);
		f.setCreateDate(DateTimeUtils.getLongTimeNow());
		f.setDescription(folderDTO.getDescription());
		if(folderDTO.getPosition() != null) {
			f.setPosition(folderDTO.getPosition());
		}
		f.setName(folderDTO.getName());
		f.setParent(parentFolder);
		f.setUpdateDate(DateTimeUtils.getLongTimeNow());
		folderRepository.save(f);

		return ErrorDTO.builder().type(ErrorType.NO_ERROR).build();
	}

	@Transactional
	public ErrorDTO updateFolder(Folder folder, FolderDTO folderDTO, Principal principal) {
		if((folderDTO.getName() == null) || folderDTO.getName().isEmpty()) {
			throw new CommonException(HttpStatus.BAD_REQUEST, "Bad folder name");
		}
		folder.setName(folderDTO.getName());
		folder.setDescription(folderDTO.getDescription());
		if(folderDTO.getPosition() != null) {
			folder.setPosition(folderDTO.getPosition());
		}
		folder.setUpdateDate(DateTimeUtils.getLongTimeNow());
		folderRepository.save(folder);
		
		return ErrorDTO.builder().type(ErrorType.NO_ERROR).build();
	}

	@Transactional
	public ErrorDTO deleteFolder(Folder folder, Principal principal) {
		folder.setActive(false);
		folderRepository.save(folder);
		return ErrorDTO.builder().type(ErrorType.NO_ERROR).build();
	}
	
	public List<BreadcrumbsDTO> getBreadcrumbs(Folder folder) {
		List<BreadcrumbsDTO> bcs = new ArrayList<>();
		Folder currentNode = folder;
		while(currentNode != null) {
			BreadcrumbsDTO bc = BreadcrumbsDTO.builder() //
					.id(currentNode.getId()) //
					.name(currentNode.getName()).build();
			if(currentNode.getPicture() != null) {
				bc.setType(BreadcrumbsNodeType.PICTURE);
			} else {
				bc.setType(BreadcrumbsNodeType.FOLDER);
			}
			bcs.add(bc);
			currentNode = currentNode.getParent();
		}
		return bcs;
	}

	@Transactional
	public ErrorDTO setAsFrontImage(Folder folder, Principal principal) {
		Folder parent = folder.getParent();
		if(parent != null) {
			parent.setFrontImage(folder);
			folderRepository.save(parent);
		}
		return ErrorDTO.builder().type(ErrorType.NO_ERROR).build();
	}

}
