package gt.app.web.mvc;

import gt.app.config.security.AppUserDetails;
import gt.app.domain.Article;
import gt.app.modules.article.*;
import gt.app.utl.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
class ArticleController {

    final ArticleService articleService;
    final CommentService commentService;

    @GetMapping({"/", ""})
    public String userHome(Model model, @AuthenticationPrincipal AppUserDetails u, Pageable pageable) {
        var page = articleService.previewAllWithFilesByUser(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending()), u.getId());
        model.addAttribute("articles", page);
        PaginationUtil.decorateModel(model, page);
        return "article";
    }

    @GetMapping("/new")
    public String startNewArticle(Model model, @AuthenticationPrincipal AppUserDetails loggedInUserDtl) {
        model.addAttribute("article", new Article()); //new article box at top
        return "article/new-article";
    }

    @PostMapping("/add")
    public String finishAddArticle(ArticleCreateDto articleDto, RedirectAttributes redirectAttrs) {

        //TODO:validate and return to GET:/add on errors

        Article article = articleService.createArticle(articleDto);

        redirectAttrs.addFlashAttribute("success", "Article with title " + article.getTitle() + " is received and being analyzed");

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("@permEvaluator.hasAccess(#id, 'Article' )")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttrs) {

        articleService.delete(id);

        redirectAttrs.addFlashAttribute("success", "Article with id " + id + " is deleted");

        return "redirect:/article/";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("@permEvaluator.hasAccess(#id, 'Article' )")
    public String startEditArticle(Model model, @PathVariable Long id) {
        model.addAttribute("msg", "Add a new article");
        model.addAttribute("article", articleService.read(id));
        return "article/edit-article";
    }

    @PostMapping("/edit")
    @PreAuthorize("@permEvaluator.hasAccess(#articleDto.id, 'Article' )")
    public String finishEditArticle(Model model, ArticleEditDto articleDto, RedirectAttributes redirectAttrs) {
        model.addAttribute("msg", "Add a new article");


        //TODO:validate and return to GET:/edit/{id} on errors


        articleService.update(articleDto);

        redirectAttrs.addFlashAttribute("success", "Article with title " + articleDto.getTitle() + " is updated");

        return "redirect:/article/";
    }

    @PostMapping("/addComment")
    public String addComment(NewCommentDto dto, RedirectAttributes redirectAttrs) {
        //TODO:validate and return to GET:/add on errors

        commentService.save(dto);

        redirectAttrs.addFlashAttribute("success", "Comment saved. Its currently under review.");

        return "redirect:/article/read/" + dto.getArticleId();
    }

    @GetMapping("/read/{id}")
    public String read(@PathVariable Long id, Model model) {
        ArticleReadDto dto = articleService.read(id);

        //TODO: fix ordering -- ordering is not consistent
        model.addAttribute("article", dto);

        return "article/read-article";
    }
}
