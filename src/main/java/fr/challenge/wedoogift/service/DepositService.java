package fr.challenge.wedoogift.service;

import fr.challenge.wedoogift.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositService {
    @Autowired
    DepositRepository depositRepository;
}
