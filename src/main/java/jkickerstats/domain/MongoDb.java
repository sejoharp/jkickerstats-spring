package jkickerstats.domain;


@Singleton
public class MongoDb {

	private Datastore datastore;
	
	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

}
