package com.esiproject2023.reviewservice.service;

import com.esiproject2023.reviewservice.dto.EmailRequest;
import com.esiproject2023.reviewservice.dto.MetadataResponse;
import com.esiproject2023.reviewservice.dto.ReviewDto;
import com.esiproject2023.reviewservice.model.Review;
import com.esiproject2023.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private WebClient.Builder webClient;

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = Review.builder()
                .userId(reviewDto.getUserId())
                .contentId(reviewDto.getContentId())
                .body(reviewDto.getBody())
                .score(reviewDto.getScore())
                .build();

        MetadataResponse[] response = webClient.build().get().uri("http://metadata-service/metadata/searchByIDs/{ids}", review.getContentId()).retrieve().bodyToMono(MetadataResponse[].class).block();
        EmailRequest emailRequest = new EmailRequest(
                "muradkhsmmdv@gmail.com",
                "Thank you very much for your review!", "You have added review",
                "\nYou have recently added review/rating to the content " + response[0].getTitle() + "\n\nWe really apprecieate your time spent for making CineMate" +
                        " better place for entertainment industry!\n\nSincerely,\nTeam CineMate!");
        Review result = reviewRepository.save(review);
        kafkaTemplate.send("emailTopic", emailRequest);

//        webClient.build().post().uri("http://email-service/email/send").body(Mono.just(emailRequest), EmailRequest.class).exchangeToMono(emailResponse -> Mono.just(emailResponse.statusCode())).block();

//        webClient.build().post().uri("http://email-service/email/send").body(Mono.just(emailRequest), EmailRequest.class).retrieve().bodyToMono(EmailRequest.class).subscribe();

        log.info("The review with id {} is added", review.getId());
        return mapToReviewDto(result);
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::mapToReviewDto).toList();
    }

    public ReviewDto mapToReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .contentId(review.getContentId())
                .body(review.getBody())
                .score(review.getScore())
                .build();
    }

    public Optional<ReviewDto> getReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(this::mapToReviewDto);
    }

    public List<Review> getReviewsForSpecificContent(String contentId) {
        return reviewRepository.findByContentId(contentId);
    }

    public void deleteReview(Long id) {
        Optional<Review> reviewToBeDeleted = reviewRepository.findById(id);
        String contentIdForDeletedReview = reviewToBeDeleted.get().getContentId();
        MetadataResponse[] response;
        if(contentIdForDeletedReview != null) {
            response = webClient.build().get().uri("http://metadata-service/metadata/searchByIDs/{ids}", contentIdForDeletedReview).retrieve().bodyToMono(MetadataResponse[].class).block();
            if(response != null) {
                //           Dynamic email here, once User Auth is done.
                EmailRequest emailRequest = new EmailRequest(
                        "muradkhasmammadov@gmail.com",
                        "We have deleted your review!", "Your review has been deleted",
                        "We have deleted your review for the content " + "'" + response[0].getTitle() + "'" + ", as it was containing inappropriate content which was against our policy regarding" +
                                " adding reviews/ratings for the content. We always try to keep our platform safe and friendly for everyone.\n\n" +
                                "Unfortunately, we will have to ban you from the platform in case of repetition of such case.\n\n" +
                                "Thank you for your understanding and cooperation.\n\n"+ "Your deleted review for the content was like:\n" + "'" + reviewToBeDeleted.get().getBody() + "'" +
                                "\n\nSincerely,\nTeam CineMate!");
                kafkaTemplate.send("emailTopic", emailRequest);
            }
        }
        reviewRepository.deleteById(id);
        log.info("Review with id {} has been deleted", id);
    }

}
