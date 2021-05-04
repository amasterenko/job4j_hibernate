package ru.job4j.hibernate.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrderStoreTest {
    private BasicDataSource pool;

    @Before
    public void setBefore() throws SQLException {
        pool = new BasicDataSource();
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void setUpAfter() throws SQLException {
        pool.getConnection().prepareStatement("DROP SCHEMA PUBLIC CASCADE").execute();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() throws SQLException {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenUpdateOrderAndFindAllThenOneRowWithNewDescription() {
        OrderStore store = new OrderStore(pool);

        Order order = store.save(Order.of("name1", "description1"));
        order.setDescription("description2");
        store.update(order);
        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description2"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenUpdateNotExistedOrderAndFindAllThenTheSameOrders() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        Order updOrder = Order.of("name2", "description2");
        updOrder.setId(2);
        store.update(updOrder);
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getName(), is("name1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenFindByIdThenOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        Order foundOrder = store.findById(2);

        assertThat(foundOrder.getDescription(), is("description2"));
        assertThat(foundOrder.getId(), is(2));
    }

    @Test
    public void whenFindByIdThenNoRows() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        Order foundOrder = store.findById(3);

        assertNull(foundOrder);
    }

    @Test
    public void whenFindByNameThenOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        Order foundOrder = store.findByName("name2");

        assertThat(foundOrder.getDescription(), is("description2"));
        assertThat(foundOrder.getId(), is(2));
    }

    @Test
    public void whenFindByNameThenNoRows() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        Order foundOrder = store.findByName("name");

        assertNull(foundOrder);
    }

    @Test
    public void whenFindAllThenTwoRowsWithDescriptions() {
        OrderStore store = new OrderStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(2));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(1).getDescription(), is("description2"));
    }

    @Test
    public void whenFindAllThenNoRows() {
        OrderStore store = new OrderStore(pool);
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(0));
    }

}