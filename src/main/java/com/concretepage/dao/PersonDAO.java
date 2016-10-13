package com.concretepage.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.concretepage.entity.Person;

@Transactional
@Repository
public class PersonDAO implements IPersonDAO {
	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void persist(Object entity) {
		getSession().persist(entity);
	}

	public void delete(Object entity) {
		getSession().delete(entity);
	}

	@Override
	public Person getPersonById(int pid) {
		return (Person) getSession().get(Person.class, pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAllPersons() {

		Criteria criteria = getSession().createCriteria(Person.class).addOrder(Order.asc("pid"));
		return (List<Person>) criteria.list();
	}

	@Override
	public boolean addPerson(Person person) {
		persist(person);
		return false;
	}

	@Override
	public void updatePerson(Person person) {
		Person p = getPersonById(person.getPid());
		p.setName(person.getName());
		p.setLocation(person.getLocation());
		persist(p);
	}

	@Override
	public void deletePerson(int pid) {
		delete(getPersonById(pid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean personExists(String name, String location) {
		Criteria criteria = getSession().createCriteria(Person.class)
				.add(Restrictions.and(Restrictions.eq("name", name), Restrictions.eq("location", location)));
		Person person = (Person) criteria.uniqueResult();
		return person != null ? true : false;
	}
}
