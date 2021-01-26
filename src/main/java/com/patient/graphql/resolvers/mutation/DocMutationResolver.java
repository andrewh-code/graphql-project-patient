package com.patient.graphql.resolvers.mutation;

import com.patient.PatientApp;
import com.patient.domain.model.Doc;
import com.patient.domain.model.graphInput.DocInput;
import com.patient.exceptions.PatientAppException;
import com.patient.repository.DocRepo;
import com.patient.service.graphql.DocService;
import graphql.GraphQLException;
import graphql.GraphqlErrorException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DocMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private DocRepo docRepo;

    @Autowired
    private DocService docService;

    public Doc createNewDoctor(DocInput docInput) {

        Doc newDoc = Doc.docBuilder()
                .firstName(docInput.getFirstName())
                .lastName(docInput.getLastName())
                .title(docInput.getTitle())
                .specialty(docInput.getSpecialty())
                .build();

        docService.createNewDoctor(newDoc);
        return newDoc;
    }

    public Doc updateDoctorInfo(Long Id, DocInput docInput) throws GraphQLException {
        Doc existingDoc;
        try {
            existingDoc = docService.retrieveDoctorById(Id);
            if (existingDoc == null) {
                throw new GraphQLException("unable to find existing doctor");
            }
            docService.updateDoctor(existingDoc, docInput);
        } catch (Exception e){
            throw new GraphQLException(e);
        }

        return existingDoc;
    }
}
