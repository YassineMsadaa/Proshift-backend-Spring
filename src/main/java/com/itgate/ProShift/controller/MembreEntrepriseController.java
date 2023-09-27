package com.itgate.ProShift.controller;

import com.itgate.ProShift.repository.MembreEntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.itgate.ProShift.payload.response.MessageResponse;
import com.itgate.ProShift.entity.MembreEntreprise;
import com.itgate.ProShift.service.CSVHelper;
import com.itgate.ProShift.service.MembreEntrepriseService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping("/membre")
public class MembreEntrepriseController {

  @Autowired
  MembreEntrepriseService memberService;

  @Autowired
  private MembreEntrepriseRepository membresOfCompanyRepo;

  @PostMapping("/upload")
  public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
    String message;

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        memberService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
  }

  @GetMapping("/getall")
  public ResponseEntity<List<MembreEntreprise>> getAllMembreEntreprise() {
    try {
      List<MembreEntreprise> tutorials = memberService.findAllMembreEntreprise();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "membres of company.csv";
    InputStreamResource file = new InputStreamResource(memberService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(file);
  }

  @DeleteMapping("/delete")
  public ResponseEntity< ? > deleteAll(){

    return ResponseEntity
            .ok()
            .body(new MessageResponse(" Deleted successfully!"));
  }


  @GetMapping("/getmembrebyrole/{role}")
  public ResponseEntity<?> findMembreEntrepriseByRole(@PathVariable String role) {
    try {
      List<MembreEntreprise> members = memberService.findMembreEntrepriseByRole(role);
      return ResponseEntity.ok().body(members);
    } catch (Exception e) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("ERROR: Failed to load the members list!"));
    }
  }

    @GetMapping("/getmembrebyrcin/{cin}")
    public ResponseEntity<?> findMembreEntrepriseByCin(@PathVariable String cin){
      if(memberService.findMembreEntrepriseByCin(cin)==null){
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("ERROR: user does not exist!"));
      }
      MembreEntreprise membreEntreprise =  memberService.findMembreEntrepriseByCin(cin);
      return ResponseEntity.ok().body(membreEntreprise);
    }

  @PutMapping("/updatemembre")
  public ResponseEntity<?> updateMembreEntreprise(@RequestBody MembreEntreprise membre){
    memberService.updateMembreEntreprise(membre);
    return ResponseEntity.ok().body(membre);
  }

  @DeleteMapping("/delete/{cin}")
  public ResponseEntity<?> removeMembreEntreprise( @PathVariable String cin) {
    try {
      if(memberService.findMembreEntrepriseByCin(cin)==null){
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("ERROR: user does not exist!"));
      }
      memberService.removeMembreEntreprise(memberService.findMembreEntrepriseByCin(cin));
      return ResponseEntity
              .ok()
              .body(new MessageResponse(" Deleted successfully!"));

    } catch (Exception e) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("ERROR: Failed to delete Client!"));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> createMembreEntreprise(@RequestBody MembreEntreprise membreEntreprise) {
    Optional<MembreEntreprise> existingMembre = Optional.ofNullable(membresOfCompanyRepo.findByCin(membreEntreprise.getCin()));
    if (existingMembre.isPresent()) {
      String errorMessage = "Le membre avec le CIN " + membreEntreprise.getCin() + " existe déjà.";
      return ResponseEntity.badRequest().body(new MessageResponse(errorMessage));
    }

    MembreEntreprise createdMembre = membresOfCompanyRepo.save(membreEntreprise);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdMembre);
  }



}
