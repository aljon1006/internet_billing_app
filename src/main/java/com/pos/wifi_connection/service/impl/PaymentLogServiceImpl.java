package com.pos.wifi_connection.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pos.wifi_connection.dto.CustomerDTO;
import com.pos.wifi_connection.dto.PaymentLogDTO;
import com.pos.wifi_connection.model.Customer;
import com.pos.wifi_connection.model.PaymentLog;
import com.pos.wifi_connection.repository.CustomerRepository;
import com.pos.wifi_connection.repository.PaymentLogRepository;
import com.pos.wifi_connection.service.PaymentLogService;

import jakarta.transaction.Transactional;

@Service
public class PaymentLogServiceImpl implements PaymentLogService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PaymentLogRepository paymentLogRepository;

    @Autowired
    PlanServiceImpl planServiceImpl;

    @Autowired
    StatusServiceImpl statusServiceImpl;

    @Override
    public Page<PaymentLogDTO> getPaymentLogs(Pageable pageable) {
        Page<PaymentLog> paymentLogPage = paymentLogRepository.findAll(pageable);
        return paymentLogPage.map(log -> {
            PaymentLogDTO dto = new PaymentLogDTO();
            dto.setBillId(log.getBillId());
            dto.setCustId(log.getCustomer().getCustId());
            dto.setAmount(log.getAmount());
            dto.setCreateDate(log.getCreateDate());
            Customer customer = customerRepository.findById(log.getCustomer().getCustId()).orElse(null);
            if (customer != null) {
                dto.setCustomerFullName(customer.getFirstName() + " " + customer.getLastName());
            }
            return dto;
        });
    }

    @Override
    public PaymentLogDTO getPaymentLogById(Integer id) {
        return paymentLogRepository.findById(id)
            .map(log -> {
                PaymentLogDTO dto = new PaymentLogDTO();
                dto.setBillId(log.getBillId());
                dto.setCustId(log.getCustomer().getCustId());
                dto.setAmount(log.getAmount());
                dto.setCreateDate(log.getCreateDate());
                Customer customer = customerRepository.findById(log.getCustomer().getCustId()).orElse(null);
                if (customer != null) {
                    dto.setCustomerFullName(customer.getFirstName() + " " + customer.getLastName());
                }
                return dto;
            })
            .orElseThrow(() -> new IllegalArgumentException("Payment log not found with ID: " + id));
    }

    @Override
    public PaymentLog savePaymentLog(PaymentLogDTO paymentLogDTO) {
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setBillId(paymentLogDTO.getBillId());
        Customer customer = customerRepository.getReferenceById(paymentLogDTO.getCustId());
        
        if (customer.getStatus() != null || customer.getStatus().getStatus() == 1) {
            customer.setStatus(statusServiceImpl.getStatus(0));
        }
        paymentLog.setAmount(paymentLogDTO.getAmount());
        paymentLog.setCustomer(customer);
        PaymentLog saved = paymentLogRepository.save(paymentLog);

        LocalDate currentDueDate = customer.getDueDate() != null ? customer.getDueDate() : LocalDate.now();
        customer.setDueDate(currentDueDate.plusDays(32));
        customerRepository.save(customer);

        return saved;

    }

    @Override
    public Customer deactivateUser(CustomerDTO customerDTO) {
        Customer customer = customerRepository.getReferenceById(customerDTO.getCustId());
        customer.setStatus(statusServiceImpl.getStatus(1));
        Customer saved = customerRepository.save(customer);
        return saved;
    }

    @Override
    public byte[] createReceipt(CustomerDTO dto, PaymentLogDTO paymentLog, Integer billId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 80mm width (226pt) x 160mm height (453pt)
        Rectangle envelope = new Rectangle(226, 453);
        Document document = new Document(envelope, 15, 15, 15, 15);

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            String receiptNo = String.format("REC-%d-%05d", LocalDate.now().getYear(), billId);
            // Use Monospace font
            Font fontTitle = FontFactory.getFont(FontFactory.COURIER_BOLD, 14);
            Font fontBold = FontFactory.getFont(FontFactory.COURIER_BOLD, 10);
            Font fontNormal = FontFactory.getFont(FontFactory.COURIER, 9);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

            // 1. Header
            Paragraph title = new Paragraph("WIFI CONNECTION", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subHeader = new Paragraph("Hagonoy, Bulacan\nOfficial Payment Receipt", fontNormal);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader);

            document.add(new Paragraph("-------------------------------", fontNormal));

            // 2. Receipt Info Table (No Borders)
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            
            infoTable.addCell(createNoBorderCell("Receipt #:", fontNormal, Element.ALIGN_LEFT));
            infoTable.addCell(createNoBorderCell(receiptNo, fontNormal, Element.ALIGN_RIGHT));
            
            infoTable.addCell(createNoBorderCell("Date:", fontNormal, Element.ALIGN_LEFT));
            LocalDateTime ldt = paymentLog.getCreateDate();
            if (ldt != null) {
                // Manually formatting to MM/DD/YYYY HH:mm
                String formattedDate = String.format("%02d/%02d/%04d %02d:%02d", 
                                        ldt.getMonthValue(), 
                                        ldt.getDayOfMonth(), 
                                        ldt.getYear(), 
                                        ldt.getHour(), 
                                        ldt.getMinute());
                                        
                infoTable.addCell(createNoBorderCell(formattedDate, fontNormal, Element.ALIGN_RIGHT));
            } else {
                infoTable.addCell(createNoBorderCell("N/A", fontNormal, Element.ALIGN_RIGHT));
            }
            
            document.add(infoTable);

            document.add(new Paragraph("-------------------------------", fontNormal));
            document.add(new Paragraph(" ", fontNormal)); // Spacer

            // 3. Customer Info
            Paragraph clientName = new Paragraph(dto.getFirstName() + " " + dto.getLastName(), fontBold);
            document.add(clientName);
            
            String addrStr = String.format("#%s %s, %s\n%s, %s", 
                dto.getHouseNumber(), 
                dto.getStreet(), 
                dto.getBarangay(), 
                dto.getMunicipality(), 
                dto.getProvince()
            );
            document.add(new Paragraph(addrStr, fontNormal));
            
            document.add(new Paragraph(" ", fontNormal)); // Spacer
            document.add(new Paragraph(" ", fontNormal)); // Spacer

            // 4. Payment Table
            PdfPTable paymentTable = new PdfPTable(2);
            paymentTable.setWidthPercentage(100);
            
            // Item Row
            paymentTable.addCell(createBottomBorderCell("Internet Service Plan", fontNormal, Element.ALIGN_LEFT));
            paymentTable.addCell(createBottomBorderCell(String.format("%,.2f", dto.getPrice()), fontNormal, Element.ALIGN_RIGHT));
            
            // Total Row
            paymentTable.addCell(createNoBorderCell("TOTAL PAID (PHP)", fontBold, Element.ALIGN_LEFT));
            paymentTable.addCell(createNoBorderCell(String.format("%,.2f", dto.getPrice()), fontBold, Element.ALIGN_RIGHT));
            document.add(paymentTable);

            document.add(new Paragraph(" ", fontNormal)); // Spacer

            // 5. Next Due Date Box
            PdfPTable dueBox = new PdfPTable(1);
            dueBox.setWidthPercentage(100);
            String dueDateStr = "NEXT DUE DATE: " + (dto.getDueDate() != null ? dto.getDueDate().format(formatter).toUpperCase() : "N/A");
            PdfPCell boxCell = new PdfPCell(new Phrase(dueDateStr, fontBold));
            boxCell.setPadding(8);
            boxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dueBox.addCell(boxCell);
            document.add(dueBox);

            // 6. Footer
            Paragraph footer = new Paragraph("\n\nThank you for your payment!", fontNormal);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    // Helper methods for table styling
    private PdfPCell createNoBorderCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingBottom(5);
        return cell;
    }

    private PdfPCell createBottomBorderCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingBottom(8);
        return cell;
    }

    @Override
    @Transactional
    public void updatePlan(Integer custId, Integer planId) {

        Customer customer =  customerRepository.findById(custId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + custId));
        customer.setPlan(planServiceImpl.getPlanById(planId));
    }

    @Override
    public Page<PaymentLogDTO> searchPaymentLogs(String searchQuery, Pageable pageable) {
        Page<PaymentLog> entities;
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            entities = paymentLogRepository.findAll(pageable);
        } else {
            entities = paymentLogRepository.searchPaymentLogs(searchQuery.trim(), pageable);
        }

        return entities.map(log -> {
            PaymentLogDTO dto = new PaymentLogDTO();
            dto.setBillId(log.getBillId());
            dto.setCustId(log.getCustomer().getCustId());
            dto.setAmount(log.getAmount());
            dto.setCreateDate(log.getCreateDate());
            Customer customer = customerRepository.findById(log.getCustomer().getCustId()).orElse(null);
            if (customer != null) {
                dto.setCustomerFullName(customer.getFirstName() + " " + customer.getLastName());
            }
            return dto;
        });
    }
        
}
