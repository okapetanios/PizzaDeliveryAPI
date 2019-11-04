package edu.neu.khoury.cs5500.dijkstraspizza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neu.khoury.cs5500.dijkstraspizza.model.price.PriceCalculator;
import edu.neu.khoury.cs5500.dijkstraspizza.model.price.GenericPriceCalculator;
import edu.neu.khoury.cs5500.dijkstraspizza.model.price.RatioDiscountSpecial;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PriceCalculatorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PriceCalculatorController.class)
@ContextConfiguration(classes =
    {TestContext.class, WebApplicationContext.class, PriceCalculatorController.class})
@WebAppConfiguration
public class PriceCalculatorControllerTest {

  @Autowired
  WebApplicationContext context;

  @Autowired
  MockMvc mockMvc;

  @MockBean
  PriceCalculatorRepository repository;

  @Autowired
  ObjectMapper mapper;

  private PriceCalculator genericNoFreeIngredients;
  private PriceCalculator genericTwoFreeIngredients;
  private PriceCalculator halfOffAll;
  private PriceCalculator bogo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    genericNoFreeIngredients = new GenericPriceCalculator();
    genericNoFreeIngredients.setId("generic-no-free-price");

    genericTwoFreeIngredients = new GenericPriceCalculator(2);
    genericTwoFreeIngredients.setId("generic-two-free-price");

    halfOffAll = new RatioDiscountSpecial(.5);
    halfOffAll.setId("half-off");

    bogo = new RatioDiscountSpecial(0, 2, 1, .5);
    bogo.setId("bogo");
  }

  private static class Behavior {
    PriceCalculatorRepository repository;

    public static Behavior set(PriceCalculatorRepository repository) {
      Behavior behavior = new Behavior();
      behavior.repository = repository;
      return behavior;
    }

    public void hasNoPriceCalculator() {
      when(repository.findAll()).thenReturn(Collections.emptyList());
      when(repository.existsById(anyString())).thenReturn(false);
    }

    public void returnSame() {
      when(repository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    public void returnPriceCalcluators(PriceCalculator... priceCalculators) {
      when(repository.findAll()).thenReturn(Arrays.asList(priceCalculators));
      when(repository.existsById(anyString())).thenAnswer(invocationOnMock -> {
        for (PriceCalculator priceCalculator: priceCalculators) {
          if (priceCalculator.getId().equals(invocationOnMock.getArguments()[0])) {
            return true;
          }
        }
        return false;
      });
      when(repository.findById(anyString())).thenAnswer(invocationOnMock -> Arrays.stream(priceCalculators)
          .filter(priceCalculator -> priceCalculator.getId().equals(invocationOnMock.getArguments()[0]))
          .collect(Collectors.collectingAndThen(Collectors.toList(),
              list -> Optional.of(list.get(0)))));
    }
  }

  @Test
  public void getAllPriceCalculators() {
  }

  @Test
  public void getPriceCalculatorByIdHttp() {
  }

  @Test
  public void newPriceCalculator() {
  }

  @Test
  public void deletePriceCalculatorById() {
  }

  @Test
  public void getPriceCalculatorById() {
  }
}