/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import entity.Partner;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PartnerSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewPartner(Partner newPartner) throws PartnerUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Partner>>constraintViolations = validator.validate(newPartner);
        
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newPartner);
                em.flush();
                
                return newPartner.getPartnerId();
            }
            catch(PersistenceException ex){
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PartnerUsernameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException {
        Partner partnerToRetrieve = em.find(Partner.class, partnerId);
        if(partnerToRetrieve != null){
            return partnerToRetrieve;
        }else{
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }
    
    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Partner)query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException{
        try {
            Partner partnerToLogin = retrievePartnerByUsername(username);
            if(partnerToLogin.getPassword().equals(password)){
                return partnerToLogin;
            }else{
                throw new InvalidLoginCredentialException("Usernmae does not exist or invalid password!");
            }
        }
        catch(PartnerNotFoundException ex){
            throw new InvalidLoginCredentialException("Usernmae does not exist or invalid password!");
        }
    }
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
