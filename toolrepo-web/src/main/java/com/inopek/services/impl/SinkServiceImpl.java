package com.inopek.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inopek.beans.AddressBean;
import com.inopek.beans.ClientBean;
import com.inopek.beans.SinkBean;
import com.inopek.beans.UserBean;
import com.inopek.dao.AddressDao;
import com.inopek.dao.ClientDao;
import com.inopek.dao.SinkDao;
import com.inopek.enums.ProfileEnum;
import com.inopek.services.SinkService;

@Service
public class SinkServiceImpl implements SinkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SinkServiceImpl.class);

	@Autowired
	private SinkDao sinkDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private ClientDao clientDao;


	@Override
	public boolean checkReferenceExists(SinkBean sink, boolean stepBefore) {
		ClientBean client = getClient(sink);
		return client == null ? false : findByReferenceAndClientAndStep(sink.getReference(), client.getName(), stepBefore);
	}

	@Override
	public HashMap<String, Boolean> prepareAndSave(Set<SinkBean> sinks, UserBean user, ProfileEnum profile) {
		HashMap<String, Boolean> fileNamesMap = new HashMap<String, Boolean>();
		sinks.forEach(sink -> {
			Optional<ClientBean> optionalClient = Optional.ofNullable(getClient(sink));
			optionalClient.ifPresent(client -> {
				Optional<SinkBean> optionalSink = Optional.ofNullable(sinkDao.findByReferenceAndClient(sink.getReference(), client.getId()));
				if(optionalSink.isPresent()) {
					// update existing sink
					updateExistingBeanByProfile(profile, fileNamesMap, sink, optionalSink.get());
				} else {
					prepareSinkForCreation(sink, user, client);
					if (sinkDao.save(sink) != null) {
						fileNamesMap.put(sink.getFileName(), true);
					}
				}

			});
		});
		return fileNamesMap;
	}

	@Override
	public SinkBean prepareAndSave(SinkBean sink, UserBean user) {
		ClientBean client = getClient(sink);
		if (client != null) {
			Optional<SinkBean> optionalSink = Optional.ofNullable(sinkDao.findByReferenceAndClient(sink.getReference(), client.getId()));
			if(optionalSink.isPresent()) {
				// update existing sink
				return updateImagesAndData(sink, user, optionalSink.get());
			} else {
				prepareSinkForCreation(sink, user, client);
				return sinkDao.save(sink);
			}
		}
		return null;
	}
	
	@Override
	public ArrayList<SinkBean> findAllSinksByDateAnClientAndReference(Date startDate, Date endDate,
			String clientName, String reference) {
		return sinkDao.findAllSinksByDateAnClientAndReference(startDate, endDate, clientName,
				reference);
	}

	@Override
	public boolean deleteSink(SinkBean sinkBean) {
		sinkDao.delete(sinkBean.getId());
		return !sinkDao.exists(sinkBean.getId());
	}

	@Override
	public SinkBean update(SinkBean sink, UserBean user) {
		ClientBean client = getClient(sink);
		if (client != null) {
			SinkBean existingSink = sinkDao.findByReferenceAndClient(sink.getReference(), client.getId());
			if (existingSink != null) {
				// update
				BeanUtils.copyProperties(sink, existingSink, "address", "client", "id");
				existingSink.setAddress(createAddress(sink));
				existingSink.setSinkUpdateDate(new Date());
				existingSink.setUserUpdate(user);
				if (sink.getImagePathBeforeClean() == null) {
					existingSink.setImagePathBeforeClean(existingSink.getImagePathBeforeClean());
				}
				if (sink.getImagePathAfterClean() == null) {
					existingSink.setImagePathAfterClean(existingSink.getImagePathAfterClean());
				}	
				return sinkDao.save(existingSink);
			}
		}
		return null;
	}

	@Override
	public boolean findByReferenceAndClientAndStep(String reference, String clientName, boolean stepBefore) {
		return sinkDao.findByReferenceAndClientAndStep(reference, clientName, stepBefore) != null;
	}

	private void setClientAndAddressAndUser(SinkBean sink, ClientBean client, UserBean user) {
		sink.setClient(client);
		sink.setUserCreation(user);
		createAddress(sink);
	}

	private AddressBean createAddress(SinkBean sink) {
		AddressBean address = sink.getAddress();
		if (address != null) {
			return addressDao.save(address);
		}
		return null;
	}

	private SinkBean copyAfterData(SinkBean sink, SinkBean existingSink) {
		existingSink.setImagePathAfterClean(sink.getImagePathAfterClean());
		existingSink.setLength(sink.getLength());
		existingSink.setObservations(sink.getObservations());
		existingSink.setPipeLineDiameterId(sink.getPipeLineDiameterId());
		existingSink.setPipeLineLength(sink.getPipeLineLength());
		existingSink.setPlumbOptionId(sink.getPlumbOptionId());
		existingSink.setSinkStatusId(sink.getSinkStatusId());
		existingSink.setSinkTypeId(sink.getSinkTypeId());
		existingSink.setAddress(createAddress(sink));
		existingSink.setSinkUpdateDate(new Date());
		return sinkDao.save(existingSink);
	}

	private SinkBean copyBeforerData(SinkBean sink, SinkBean existingSink) {
		existingSink.setImagePathBeforeClean(sink.getImagePathBeforeClean());
		existingSink.setSinkUpdateDate(new Date());
		return sinkDao.save(existingSink);
	}

	private ClientBean getClient(SinkBean sink) {
		ClientBean client = null;
		if (sink.getClient() != null) {
			String clientName = sink.getClient().getName();
			client = clientDao.findByName(clientName);
			if (client == null) {
				LOGGER.warn(String.format("The client %s is not known", clientName));
			}
		}
		return client;
	}

	private void controlDataBeforeClean(HashMap<String, Boolean> fileNamesMap, SinkBean sink, SinkBean existingSink) {
		boolean hasImageBeforeClean = existingSink.getImagePathBeforeClean() != null;
		if (!hasImageBeforeClean) {
			// ref exists but not image before then update
			copyBeforerData(sink, existingSink);
		}
		fileNamesMap.put(sink.getFileName(), !hasImageBeforeClean);
	}
	
	private void controlDataAfterClean(HashMap<String, Boolean> fileNamesMap, SinkBean sink, SinkBean existingSink) {
		boolean hasImageAfterClean = existingSink.getImagePathAfterClean() != null;
		if (!hasImageAfterClean) {
			// ref exists but not image after then update
			copyAfterData(sink, existingSink);
		}
		fileNamesMap.put(sink.getFileName(), !hasImageAfterClean);
	}
	
	private void prepareSinkForCreation(SinkBean sink, UserBean user, ClientBean client) {
		setClientAndAddressAndUser(sink, client, user);
		sink.setSinkCreationDate(new Date());
	}
	
	private SinkBean updateImagesAndData(SinkBean sink, UserBean user, SinkBean existingSink) {
		existingSink.setUserUpdate(user);
		if (sink.getImagePathAfterClean() != null) {
			return copyAfterData(sink, existingSink);
		} else if (sink.getImagePathBeforeClean() != null) {
			return copyBeforerData(sink, existingSink);
		}
		return null;
	}
	
	private void updateExistingBeanByProfile(ProfileEnum profile, HashMap<String, Boolean> fileNamesMap, SinkBean sink,
			SinkBean existingSink) {
		if (ProfileEnum.BEGIN.equals(profile)) {
			controlDataBeforeClean(fileNamesMap, sink, existingSink);
		} else if (ProfileEnum.END.equals(profile)) {
			controlDataAfterClean(fileNamesMap, sink, existingSink);
		}
	}
}
