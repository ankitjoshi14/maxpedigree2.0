package restapi.service;

import java.io.Serializable;
import java.sql.*;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CultivarIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

		String prefix = "Z";
		Connection connection = session.connection();
		Serializable id = null;

		try {

			 id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object,
					session);

			if (id == null) {
				Statement statement = connection.createStatement();

				ResultSet rs = statement
						.executeQuery("select max(cultivar_id) as Id from Cultivar where cultivar_id SIMILAR TO 'Z[0-9]{7}'");

				if (rs.next()) {
					String value = rs.getString(1);
					int idd = Integer.parseInt(value.substring(1));
					int newId = idd + 1;
					int count = countdigits(newId);
					String padding = "0000000";
					String generatedId = prefix + padding.substring(count) + newId;
					System.out.println("Generated Id: " + generatedId);
					return generatedId;
				} else {

					String padding = "0000000";
					String generatedId = prefix + padding;
					System.out.println("Generated Id: " + generatedId);
					return generatedId;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

		return id;
	}

	private int countdigits(int number) {
		int count = 0;
		for (; number != 0; number /= 10, ++count)
			;
		return count;
	}

}
